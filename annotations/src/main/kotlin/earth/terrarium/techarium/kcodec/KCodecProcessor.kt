package earth.terrarium.techarium.kcodec

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import earth.terrarium.techarium.kcodec.annotations.GenerateCodec

class KCodecProcessor(
    private val generator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private var ran = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (ran) return emptyList()
        ran = true

        val codecs = DefaultCodecs.getCodecs(resolver)
        val annotated = resolver.getSymbolsWithAnnotation(GenerateCodec::class.qualifiedName!!).toList()

        val file = FileSpec.builder("earth.terrarium.techarium.kcodec.generated", "KCodec")
            .addType(TypeSpec.objectBuilder("KCodec").apply {
                this.addFunction(FunSpec.builder("getCodec").apply {
                    this.addModifiers(KModifier.INLINE)
                    this.addTypeVariable(TypeVariableName("T").copy(reified = true))
                    this.returns(
                        ClassName("com.mojang.serialization", "Codec")
                            .parameterizedBy(TypeVariableName("T"))
                    )
                    this.addCode("return getCodec(T::class.java) as Codec<T>")
                }.build())

                this.addFunction(FunSpec.builder("getCodec").apply {
                    this.addParameter("clazz", ClassName("java.lang", "Class").parameterizedBy(STAR))
                    this.returns(ClassName("com.mojang.serialization", "Codec").parameterizedBy(STAR))
                    this.addCode("return when (clazz) {\n")
                    for ((type, codec) in codecs) {
                        this.addCode("    %T::class.java -> ${codec}\n", type)
                    }
                    this.addCode("    else -> throw IllegalArgumentException(\"Unknown codec for class: \$clazz\")\n")
                    this.addCode("}\n")
                }.build())

            }.build())

        file.build().writeTo(generator, false)

        return emptyList()
    }

}

class KCodecProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return KCodecProcessor(environment.codeGenerator, environment.logger)
    }
}