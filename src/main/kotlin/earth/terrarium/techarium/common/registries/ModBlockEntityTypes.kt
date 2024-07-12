package earth.terrarium.techarium.common.registries

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry
import com.teamresourceful.resourcefullibkt.common.getValue
import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.blocks.entities.machines.BasicDeployChildBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType

object ModBlockEntityTypes {
    val registry: ResourcefulRegistry<BlockEntityType<*>> = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, TechariumConstants.MOD_ID)

    val basicDeployChildBlockEntity: BlockEntityType<BasicDeployChildBlockEntity> by registry.register("basic_deploy_child_blockentity") {BlockEntityType.Builder.of({pos, state -> BasicDeployChildBlockEntity(basicDeployChildBlockEntity, pos, state)
    }, ModBlocks.basicDeployChildBlock).build(null)}
}