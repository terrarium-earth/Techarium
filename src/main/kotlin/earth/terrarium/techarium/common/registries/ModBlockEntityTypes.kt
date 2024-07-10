package earth.terrarium.techarium.common.registries

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry
import earth.terrarium.techarium.common.TechariumConstants
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType

object ModBlockEntityTypes {
    val registry: ResourcefulRegistry<BlockEntityType<*>> = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TechariumConstants.MOD_ID)
}