package earth.terrarium.techarium.common.registries

import com.mojang.serialization.Codec
import com.teamresourceful.bytecodecs.base.ByteCodec
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry
import com.teamresourceful.resourcefullibkt.common.component
import com.teamresourceful.resourcefullibkt.common.getValue
import com.teamresourceful.resourcefullibkt.common.persistent
import com.teamresourceful.resourcefullibkt.common.synced
import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.utils.ComponentSlot
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries

object ModComponents {

    val registry: ResourcefulRegistry<DataComponentType<*>> =
        ResourcefulRegistries.create(BuiltInRegistries.DATA_COMPONENT_TYPE, TechariumConstants.MOD_ID)

    val tankCapacity: DataComponentType<Map<ComponentSlot, Int>> by registry.register("tank_capacity") {
        component {
            persistent = Codec.unboundedMap(ComponentSlot.CODEC, Codec.INT)
            synced = ByteCodec.mapOf(ComponentSlot.BYTE_CODEC, ByteCodec.INT)
        }
    }

    val sprinklerRadius: DataComponentType<Int> by registry.register("sprinkler_radius") {
        component {
            persistent = Codec.intRange(1, 16)
            synced = ByteCodec.VAR_INT
        }
    }
}