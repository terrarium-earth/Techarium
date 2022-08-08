package earth.terrarium

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import org.objectweb.asm.*
import org.objectweb.asm.tree.*
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*

abstract class ProcessClasses : DefaultTask() {
    abstract val extensionPackages: ListProperty<String>
        @Input get

    abstract val annotationType: Property<String>
        @Input get

    abstract val classesDirectory: DirectoryProperty
        @InputDirectory get

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
        val type = Type.getType(desc)
        val returnType = type.returnType.replaceExtensionReferences(extensionName, baseName)
        val argumentTypes = type.argumentTypes.map { it.replaceExtensionReferences(extensionName, baseName) }

        desc = Type.getMethodType(returnType, *argumentTypes.toTypedArray()).descriptor

        for (instruction in instructions) {
            instruction.replaceExtensionReferences(extensionName, baseName)
        }
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
                        val returnType = bsmDescriptor.returnType.replaceExtensionReferences(extensionName, baseName)
                        val argumentTypes = bsmDescriptor.argumentTypes.map { it.replaceExtensionReferences(extensionName, baseName) }

                        Type.getMethodType(returnType, *argumentTypes.toTypedArray()).descriptor
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
                val methodDescriptor = Type.getMethodType(desc)
                val returnType = methodDescriptor.returnType.replaceExtensionReferences(extensionName, baseName)
                val argumentTypes = methodDescriptor.argumentTypes.map { it.replaceExtensionReferences(extensionName, baseName) }

                desc = Type.getMethodType(returnType, *argumentTypes.toTypedArray()).descriptor

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

    @TaskAction
    fun process() {
        val marker = project.layout.buildDirectory.dir("processedClasses").get().file(name).asFile.toPath()

        if (marker.notExists()) {
            // key: the file to be replaced with a processed version
            // value: a pair of the new class node and the extension path, to be deleted
            val modified = hashMapOf<Path, Pair<ClassNode, Path>>()
            val packagesRemaining = mutableListOf<Path>()

            val annotationDescriptor = "L${annotationType.get().replace(".", "/")};"

            for (extensionPackage in extensionPackages.get()) {
                val packagePath = classesDirectory.dir(extensionPackage.replace('.', '/')).get().asFile.toPath()
                var deletePackage = true

                if (packagePath.notExists()) {
                    throw FileNotFoundException("Extension package $extensionPackage not found")
                }

                Files.walk(packagePath).use { stream ->
                    for (path in stream.filter(Path::isRegularFile)) {
                        val extensionNode = ClassNode()
                        val reader = path.inputStream().use(::ClassReader)
                        reader.accept(extensionNode, 0)

                        if (extensionNode.access and Opcodes.ACC_ANNOTATION != 0 || extensionNode.sourceFile == "package-info.java") {
                            deletePackage = false
                            continue
                        }

                        val annotation = extensionNode.invisibleAnnotations?.firstOrNull { it.desc == annotationDescriptor }
                        if (annotation == null) {
                            throw UnsupportedOperationException("File $path in $extensionPackage does not contain the ${this.annotationType.get()} annotation")
                        }

                        val value = annotation.values[annotation.values.indexOfFirst { it == "value" } + 1] as Type
                        val basePath = classesDirectory.file("${value.internalName}.class").get().asFile

                        if (!basePath.exists()) {
                            throw FileNotFoundException("Type $value not found")
                        }

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
                            if (method.name == "<cinit>") {
                                val staticInitializer = baseNode.methods.firstOrNull { it.name == "<cinit>" }

                                if (staticInitializer == null) {
                                    baseNode.methods.add(method)
                                } else {
                                    val beforeReturn = staticInitializer.instructions.get(staticInitializer.instructions.size() - 1)

                                    repeat(method.instructions.size() - 1) {
                                        val instruction = method.instructions.get(it)

                                        staticInitializer.instructions.insert(beforeReturn, instruction)
                                    }
                                }
                            } else if (method.name != "<init>") {
                                val index = baseNode.methods.indexOfFirst { it.name == method.name }

                                if (index < 0) {
                                    baseNode.methods.add(method)
                                } else {
                                    baseNode.methods[index] = method
                                }
                            }
                        }

                        for (method in baseNode.methods) {
                            method.replaceExtensionReferences(extensionNode.name, baseNode.name)
                        }

                        modified[basePath.toPath()] = baseNode to path
                    }
                }

                if (deletePackage) {
                    packagesRemaining.add(packagePath)
                }
            }

            // Only process if no errors have happened
            for ((basePath, entry) in modified) {
                val (node, path) = entry

                val writer = ClassWriter(0)
                node.accept(writer)
                basePath.writeBytes(writer.toByteArray())

                path.deleteExisting()
            }

            for (packagePath in packagesRemaining) {
                packagePath.deleteExisting()
            }

            marker.parent.createDirectories()
            marker.createFile()
        }
    }
}
