package earth.terrarium.techarium.common.blocks.base

import earth.terrarium.techarium.common.registries.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.state.BlockState

abstract class DeployableMachine(properties: Properties) : MachineBlock(properties) {
    abstract val relativeChildPositions: Set<BlockPos>

    open fun getChildrenPositions(pos: BlockPos, state: BlockState): Set<BlockPos> =
        relativeChildPositions.mapTo(hashSetOf()) { pos.offset(it) }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos) =
        getChildrenPositions(pos, state).all { level.getBlockState(pos).canBeReplaced() }

    override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
        super.onPlace(state, level, pos, oldState, movedByPiston)
        for (blockPos in getChildrenPositions(pos, state)) {
            level.setBlockAndUpdate(blockPos, ModBlocks.basicDeployChildBlock.defaultBlockState())
        }
    }

    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        super.destroy(level, pos, state)
        for (blockPos in getChildrenPositions(pos, state)) {
            if (level.getBlockState(blockPos).`is`(ModBlocks.basicDeployChildBlock.defaultBlockState().block)) {
                level.destroyBlock(blockPos, false)
            }
        }
    }
}