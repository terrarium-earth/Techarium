package earth.terrarium.techarium.common.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

abstract class DeployableMachine(properties: Properties) : MachineBlock(properties) {
    abstract val relativeChildBlocks: Map<BlockPos, BlockState>

    open fun getChildren(pos: BlockPos, state: BlockState) = relativeChildBlocks.mapKeys { pos.offset(it.key) }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos) =
        getChildren(pos, state).all { level.getBlockState(pos).canBeReplaced() }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        super.onPlace(state, level, pos, oldState, movedByPiston)
        for ((blockPos, blockState) in getChildren(pos, state)) {
            level.setBlockAndUpdate(blockPos, blockState)
        }
    }

    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        super.destroy(level, pos, state)
        for ((blockPos, blockState) in getChildren(pos, state)) {
            if (level.getBlockState(blockPos).`is`(blockState.block))
            level.destroyBlock(blockPos, false)
        }
    }
}