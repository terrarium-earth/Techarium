package earth.terrarium.techarium.common.utils

import com.mojang.serialization.Codec
import com.teamresourceful.bytecodecs.base.ByteCodec
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec

enum class ComponentSlot {
    INPUT,
    OUTPUT,
    ;

    fun canInput() = this == INPUT

    fun canOutput() = this == OUTPUT

    fun asComponent(amount: Int) = mapOf(this to amount)

    companion object {
        val CODEC: Codec<ComponentSlot> = EnumCodec.of(ComponentSlot::class.java)
        val BYTE_CODEC: ByteCodec<ComponentSlot> = ByteCodec.ofEnum(ComponentSlot::class.java)
    }
}