package earth.terrarium.techarium.common.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.level.block.state.BlockState

abstract class DeployableMachine(properties: Properties) : MachineBlock(properties) {
    abstract val childBlocks: List<Vec3i>

    open fun getChildrenPositions(pos: BlockPos, state: BlockState) = childBlocks.map { pos.offset(it) }
}