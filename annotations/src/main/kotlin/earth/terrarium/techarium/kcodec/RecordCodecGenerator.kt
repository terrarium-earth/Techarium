package earth.terrarium.techarium.kcodec

import com.google.devtools.ksp.isPublic
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import java.util.*

object RecordCodecGenerator {

    private const val MAX_PARAMETERS = 16
    private val CODEC_TYPE = ClassName("com.mojang.serialization", "Codec")
    private val RECORDCODEC_TYPE = ClassName("com.mojang.serialization.codecs", "RecordCodecBuilder")
    private val CODECEXTRAS_TYPE = ClassName("com.teamresourceful.resourcefullib.common.codecs", "CodecExtras")

    fun isValid(declaration: KSAnnotated?, logger: KSPLogger): Boolean {
        if (declaration !is KSClassDeclaration) {
            logger.error("Declaration is not a class")
        } else if (declaration.modifiers.contains(Modifier.INLINE)) {
            logger.error("@GenerateCodec can only be applied to non-inline classes")
        } else if (!declaration.isPublic()) {
            logger.error("@GenerateCodec can only be applied to public classes")
        } else if (Modifier.DATA !in declaration.modifiers) {
            logger.error("@GenerateCodec can only be applied to data classes")
        } else if (declaration.primaryConstructor == null) {
            logger.error("@GenerateCodec can only be applied to classes with a primary constructor")
        } else if (declaration.primaryConstructor!!.parameters.isEmpty()) {
            logger.error("@GenerateCodec can only be applied to classes with a primary constructor that has parameters")
        } else if (declaration.primaryConstructor!!.parameters.size > MAX_PARAMETERS) {
            logger.error("@GenerateCodec can only be applied to classes with a primary constructor that has at most $MAX_PARAMETERS parameters")
        } else if (declaration.primaryConstructor!!.parameters.any { it.isVararg }) {
            logger.error("@GenerateCodec can only be applied to classes with a primary constructor that does not have varargs")
        } else if (declaration.primaryConstructor!!.parameters.any { it.hasDefault && it.type.resolve().isMarkedNullable }) {
            logger.error("@GenerateCodec can only be applied to classes with a primary constructor that does not have nullable parameters with default values")
        } else {
            return true
        }
        return false
    }

    private fun CodeBlock.Builder.createEntry(parameter: KSValueParameter, declaration: KSClassDeclaration): Pair<String, Type> {
        val name = parameter.name!!.asString()
        val nullable = parameter.type.resolve().isMarkedNullable
        val ksType = parameter.type.resolve()
        val type = ksType.toTypeName().copy(nullable = false)

        val builder: StringBuilder = StringBuilder()
        val args = mutableListOf<Any>()

        when(ksType.starProjection().toClassName()) {
            List::class.asClassName() -> {
                builder.append("getCodec<%T>().listOf()")
                args.add(ksType.arguments.getType(0))
            }
            Set::class.asClassName() -> {
                builder.append("%T.set(getCodec<%T>())")
                args.add(CODECEXTRAS_TYPE)
                args.add(ksType.arguments.getType(0))
            }
            Map::class.asClassName() -> {
                builder.append("%T.unboundedMap(getCodec<%T>(), getCodec<%T>())")
                args.add(CODEC_TYPE)
                args.add(ksType.arguments.getType(0))
                args.add(ksType.arguments.getType(1))
            }
            else -> {
                builder.append("getCodec<%T>()")
                args.add(type)
            }
        }

        return when {
            parameter.hasDefault -> {
                builder.append(".optionalFieldOf(\"%L\").forGetter { getter -> %T.of(getter.%L) },\n")
                args.add(name)
                args.add(Optional::class.java)
                args.add(name)
                add(builder.toString(), *args.toTypedArray())
                name to Type.DEFAULT
            }
            nullable -> {
                builder.append(".optionalFieldOf(\"%L\").forGetter { getter -> %T.ofNullable(getter.%L) },\n")
                args.add(name)
                args.add(Optional::class.java)
                args.add(name)
                add(builder.toString(), *args.toTypedArray())
                name to Type.NULLABLE
            }
            else -> {
                builder.append(".fieldOf(\"%L\").forGetter(%T::%L),\n")
                args.add(name)
                args.add(declaration.toClassName())
                args.add(name)
                add(builder.toString(), *args.toTypedArray())
                name to Type.NORMAL
            }
        }
    }

    fun generateCodec(declaration: KSAnnotated): PropertySpec {
        if (declaration !is KSClassDeclaration) {
            throw IllegalArgumentException("Declaration is not a class")
        }
        val codecName = declaration.simpleName.asString() + "Codec"
        return PropertySpec.builder(codecName, CODEC_TYPE.parameterizedBy(declaration.toClassName()))
            .addModifiers(KModifier.PRIVATE)
            .initializer(
                CodeBlock.builder().apply {
                    add("%T.create {\n", RECORDCODEC_TYPE)
                    indent()
                    add("it.group(\n")
                    val args = mutableListOf<Pair<String, Type>>()

                    indent()
                    for (parameter in declaration.primaryConstructor!!.parameters) {
                        createEntry(parameter, declaration).let { args.add(it) }
                    }
                    unindent()

                    add(").apply(it) { ${args.joinToString(", ") { "p_${it.first}" }} -> \n")

                    indent()
                    add("var obj = %T(${args.filter { it.second != Type.DEFAULT }.joinToString(", ") { 
                        val name = it.first
                        if (it.second == Type.NULLABLE) "$name = p_$name.orElse(null)" else "$name = p_$name"
                    }})\n", declaration.toClassName())
                    for (pair in args.filter { it.second == Type.DEFAULT }) {
                        val name = pair.first
                        add("if (p_$name.isPresent) obj = obj.copy($name = p_$name.get())\n")
                    }
                    add("obj\n")
                    unindent()

                    add("}\n")
                    unindent()
                    add("}\n")
                }.build()
            )
            .build()
    }

    enum class Type {
        DEFAULT,
        NULLABLE,
        NORMAL
    }

    private fun List<KSTypeArgument>.getType(index: Int): TypeName {
        return this[index].type!!.resolve().toTypeName().copy(nullable = false)
    }
}