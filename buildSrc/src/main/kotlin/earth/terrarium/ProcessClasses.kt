package earth.terrarium

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.function.Predicate
import kotlin.io.path.*

@CacheableTask
abstract class ProcessClasses : DefaultTask() {
    abstract val extensionPackages: ListProperty<String>
        @Input get

    abstract val annotationType: Property<String>
        @Input get

    abstract val classesDirectory: DirectoryProperty
        @InputDirectory
        @PathSensitive(PathSensitivity.RELATIVE)
        get

    abstract val destinationDirectory: DirectoryProperty
        @OutputDirectory get

    init {
        apply {
            extensionPackages.finalizeValueOnRead()
            annotationType.finalizeValueOnRead()
            classesDirectory.finalizeValueOnRead()
        }
    }

    private fun Type.replaceExtensionReferences(extensionName: String, baseName: String): Type = if (sort == Type.ARRAY) {
        Type.getType("[${elementType.replaceExtensionReferences(extensionName, baseName).descriptor}")
    } else if (sort == Type.OBJECT && internalName == extensionName) {
        Type.getObjectType("L$baseName;")
    } else {
        this
    }

    private fun MethodNode.replaceExtensionReferences(extensionName: String, baseName: String) {
        desc = Type.getMethodType(desc).replaceMethodDescriptor(extensionName, baseName).descriptor

        for (instruction in instructions) {
            instruction.replaceExtensionReferences(extensionName, baseName)
        }
    }

    private fun Type.replaceMethodDescriptor(extensionName: String, baseName: String): Type {
        val returnType = returnType.replaceExtensionReferences(extensionName, baseName)
        val argumentTypes = argumentTypes.map { it.replaceExtensionReferences(extensionName, baseName) }

        return Type.getMethodType(returnType, *argumentTypes.toTypedArray())
    }

    private fun AbstractInsnNode.replaceExtensionReferences(extensionName: String, baseName: String) {
        val internalExtensionName = "L$extensionName;"
        val internalBaseName = "L$baseName;"

        when (this) {
            is FieldInsnNode -> {
                if (owner == extensionName) {
                    owner = baseName
                }

                if (desc == internalExtensionName) {
                    desc = internalBaseName
                }
            }
            is FrameNode -> {
                local = local?.map {
                    if (it is String) {
                        Type.getObjectType(it).replaceExtensionReferences(extensionName, baseName).descriptor
                    } else {
                        it
                    }
                }

                stack = stack?.map {
                    if (it is String) {
                        Type.getObjectType(it).replaceExtensionReferences(extensionName, baseName).descriptor
                    } else {
                        it
                    }
                }
            }
            is InvokeDynamicInsnNode -> {
                fun fixHandle(handle: Handle): Handle {
                    val bsmDescriptor = Type.getType(handle.desc)

                    val newOwner = if (handle.owner == extensionName) {
                        baseName
                    } else {
                        handle.owner
                    }

                    val newDesc = if (bsmDescriptor.sort == Type.METHOD) {
                        bsmDescriptor.replaceMethodDescriptor(extensionName, baseName).descriptor
                    } else {
                        bsmDescriptor.replaceExtensionReferences(extensionName, baseName).descriptor
                    }

                    return Handle(handle.tag, newOwner, handle.name, newDesc, handle.isInterface)
                }

                bsm = fixHandle(bsm)

                bsmArgs = bsmArgs.map {
                    if (it is Type) {
                        it.replaceExtensionReferences(extensionName, baseName)
                    } else if (it is Handle) {
                        fixHandle(it)
                    } else {
                        it
                    }
                }.toTypedArray()
            }
            is LdcInsnNode -> {
                val cst = cst
                if (cst is Type) {
                    this.cst = cst.replaceExtensionReferences(extensionName, baseName)
                }
            }
            is MethodInsnNode -> {
                desc = Type.getMethodType(desc).replaceMethodDescriptor(extensionName, baseName).descriptor

                if (owner == extensionName) {
                    owner = baseName
                }
            }
            is MultiANewArrayInsnNode -> {
                desc = Type.getType(desc).replaceExtensionReferences(extensionName, baseName).descriptor
            }
            is TypeInsnNode -> {
                desc = Type.getObjectType(desc).replaceExtensionReferences(extensionName, baseName).internalName
            }
        }
    }

    private fun addMethod(node: ClassNode, method: MethodNode) {
        if (method.name == "<cinit>") {
            val staticInitializer = node.methods.firstOrNull { it.name == "<cinit>" }

            if (staticInitializer == null) {
                node.methods.add(method)
            } else {
                val beforeReturn = staticInitializer.instructions.get(staticInitializer.instructions.size() - 1)

                repeat(method.instructions.size() - 1) {
                    val instruction = method.instructions.get(it)

                    staticInitializer.instructions.insert(beforeReturn, instruction)
                }
            }
        } else if (method.name != "<init>") {
            val index = node.methods.indexOfFirst { it.name == method.name }

            if (index < 0) {
                node.methods.add(method)
            } else {
                node.methods[index] = method
            }
        }
    }

    @TaskAction
    fun process() {
        // key: the file to be replaced with a processed version
        // value: a pair of the new class node and the extension path, to be deleted
        val processed = hashSetOf<Path>()

        val annotationDescriptor = "L${annotationType.get().replace(".", "/")};"

        val classes = classesDirectory.asFile.get().toPath()
        val destination = destinationDirectory.asFile.get().toPath()

        for (extensionPackage in extensionPackages.get()) {
            val packagePath = classes.resolve(extensionPackage.replace('.', '/'))

            if (packagePath.notExists()) {
                throw FileNotFoundException("Extension package $extensionPackage not found")
            }

            Files.walk(packagePath).use { stream ->
                for (path in stream.filter(Path::isRegularFile)) {
                    val extensionNode = ClassNode()
                    val reader = path.inputStream().use(::ClassReader)
                    reader.accept(extensionNode, 0)

                    if (extensionNode.access and Opcodes.ACC_ANNOTATION != 0 || extensionNode.sourceFile == "package-info.java") {
                        continue
                    }

                    val annotation = extensionNode.invisibleAnnotations?.firstOrNull { it.desc == annotationDescriptor }
                    if (annotation == null) {
                        throw UnsupportedOperationException("File $path in $extensionPackage does not contain the ${this.annotationType.get()} annotation")
                    }

                    val value = annotation.values[annotation.values.indexOfFirst { it == "value" } + 1] as Type
                    val baseRelativePath = "${value.internalName}.class"
                    val basePath = classes.resolve(baseRelativePath)

                    if (!basePath.exists()) {
                        throw FileNotFoundException("Type $value not found")
                    }

                    val outputPath = destination.resolve(baseRelativePath)
                    val baseNode = ClassNode()
                    val baseReader = basePath.inputStream().use(::ClassReader)

                    baseReader.accept(baseNode, 0)

                    if (baseNode.interfaces == null) {
                        baseNode.interfaces = extensionNode.interfaces
                    } else if (extensionNode.interfaces != null) {
                        baseNode.interfaces = (baseNode.interfaces + extensionNode.interfaces).distinct()
                    }

                    for (field in extensionNode.fields) {
                        if (baseNode.fields.none { it.name == field.name }) {
                            baseNode.fields.add(field)
                        }
                    }

                    for (method in extensionNode.methods) {
                        addMethod(baseNode, method)
                    }

                    for (method in baseNode.methods) {
                        method.replaceExtensionReferences(extensionNode.name, baseNode.name)
                    }

                    processed.add(basePath)
                    processed.add(path)

                    val writer = ClassWriter(0)
                    baseNode.accept(writer)

                    outputPath.parent.createDirectories()
                    outputPath.writeBytes(writer.toByteArray())
                }
            }
        }

        copyRemainingFiles(classes, destination, processed)
    }

    private fun copyRemainingFiles(root: Path, destination: Path, processed: Set<Path>) {
        Files.walk(root).filter(Path::isRegularFile).filter(Predicate<Path>(processed::contains).negate()).use {
            for (path in it) {
                val newPath = destination.resolve(root.relativize(path).toString())

                newPath.parent.createDirectories()
                path.copyTo(newPath)
            }
        }
    }
}
