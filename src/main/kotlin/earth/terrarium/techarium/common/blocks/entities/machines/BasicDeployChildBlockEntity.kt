package earth.terrarium.techarium.common.blocks.entities.machines

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

open class BasicDeployChildBlockEntity(type: BlockEntityType<*>, pos: BlockPos, blockState: BlockState): BlockEntity(type, pos,
    blockState, ) {
    lateinit var parentPos: BlockPos
}