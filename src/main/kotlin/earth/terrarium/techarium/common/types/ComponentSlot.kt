package earth.terrarium.techarium.common.types

import com.mojang.serialization.Codec
import com.teamresourceful.bytecodecs.base.ByteCodec
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec

enum class ComponentSlot {
    INPUT,
    OUTPUT,
    ;

    companion object {
        val CODEC: Codec<ComponentSlot> = EnumCodec.of(ComponentSlot::class.java)
        val BYTE_CODEC: ByteCodec<ComponentSlot> = ByteCodec.ofEnum(ComponentSlot::class.java)
    }
}