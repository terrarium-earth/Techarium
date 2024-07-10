package earth.terrarium.techarium.common.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

abstract class DeployableMachine(properties: Properties) : MachineBlock(properties) {
    abstract val relativeChildBlocks: Map<BlockPos, BlockState>

    open fun getChildrenPositions(pos: BlockPos, state: BlockState) = relativeChildBlocks.map { pos.offset(it.key) }
}