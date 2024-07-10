package earth.terrarium.techarium.common.registries

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry
import earth.terrarium.techarium.common.TechariumConstants
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.Block

object ModBlocks {
    val registry: ResourcefulRegistry<Block> = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, TechariumConstants.MOD_ID)
}