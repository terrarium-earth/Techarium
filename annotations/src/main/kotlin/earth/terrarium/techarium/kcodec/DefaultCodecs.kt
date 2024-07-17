package earth.terrarium.techarium.kcodec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

object DefaultCodecs {

    val codecs: MutableMap<TypeName, String> = mutableMapOf()
    val stringCodecs: MutableSet<TypeName> = mutableSetOf()

    init {
        this.add("kotlin", "String", "com.mojang.serialization.Codec.STRING") { isString = true }
        this.add("kotlin", "Boolean", "com.mojang.serialization.Codec.BOOL")
        this.add("kotlin", "Byte", "com.mojang.serialization.Codec.BYTE")
        this.add("kotlin", "Short", "com.mojang.serialization.Codec.SHORT")
        this.add("kotlin", "Int", "com.mojang.serialization.Codec.INT")
        this.add("kotlin", "Long", "com.mojang.serialization.Codec.LONG")
        this.add("kotlin", "Float", "com.mojang.serialization.Codec.FLOAT")
        this.add("kotlin", "Double", "com.mojang.serialization.Codec.DOUBLE")
        this.add("java.util", "UUID", "net.minecraft.core.UUIDUtil.STRING_CODEC") { isString = true }
        this.add("net.minecraft.resources", "ResourceLocation") { isString = true }

        this.add("net.minecraft.world.item", "ItemStack")
        this.add("net.minecraft.world.item", "Item", "net.minecraft.core.registries.BuiltInRegistries.ITEM.byNameCodec()") { isString = true }

        this.add("net.neoforged.neoforge.fluids", "FluidStack")
        this.add("net.minecraft.world.level.material", "Fluid", "net.minecraft.core.registries.BuiltInRegistries.FLUID.byNameCodec()") { isString = true }
    }

    private fun add(packageName: String, className: String, arguments: Arguments.() -> Unit = {}) {
        add(packageName, className, "$packageName.$className.CODEC", arguments)
    }

    private fun add(packageName: String, className: String, codec: String, arguments: Arguments.() -> Unit = {}) {
        codecs[ClassName(packageName, className)] = codec
        val args = Arguments().apply(arguments)
        if (args.isString) stringCodecs.add(ClassName(packageName, className))
    }

    private class Arguments {
        var isString: Boolean = false
    }

}