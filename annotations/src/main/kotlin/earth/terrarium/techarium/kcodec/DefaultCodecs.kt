package earth.terrarium.techarium.kcodec

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.ksp.toClassName

object DefaultCodecs {

    fun getCodecs(resolver: Resolver): Map<TypeName, String> = Builder(resolver).apply {
        this.add("kotlin.String", "com.mojang.serialization.Codec.STRING")
        this.add("kotlin.Boolean", "com.mojang.serialization.Codec.BOOL")
        this.add("kotlin.Byte", "com.mojang.serialization.Codec.BYTE")
        this.add("kotlin.Short", "com.mojang.serialization.Codec.SHORT")
        this.add("kotlin.Int", "com.mojang.serialization.Codec.INT")
        this.add("kotlin.Long", "com.mojang.serialization.Codec.LONG")
        this.add("kotlin.Float", "com.mojang.serialization.Codec.FLOAT")
        this.add("kotlin.Double", "com.mojang.serialization.Codec.DOUBLE")
    }.build()

}

private class Builder(private val resolver: Resolver) {

    private val map = mutableMapOf<TypeName, String>()

    fun add(type: String, codec: String) {
        map[resolver.getClassDeclarationByName(type)!!.asStarProjectedType().toClassName()] = codec
    }

    fun build(): Map<TypeName, String> = map
}