package earth.terrarium.techarium.common.blocks.base

import earth.terrarium.techarium.common.util.toRotation
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

abstract class DirectionalDeployableMachine(properties: Properties): DeployableMachine(properties) {
    companion object{
        val FACING = HorizontalDirectionalBlock.FACING
    }

    init {
        registerDefaultState(defaultBlockState()
            .setValue(FACING, Direction.NORTH))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(FACING)
    }

    override fun rotate(state: BlockState, level: LevelAccessor, pos: BlockPos, direction: Rotation): BlockState =
        state.setValue(FACING, direction.rotate(state.getValue(FACING)))

    override fun mirror(state: BlockState, mirror: Mirror): BlockState =
        state.rotate(mirror.getRotation(state.getValue(FACING)))

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? =
        super.getStateForPlacement(context)?.setValue(FACING, context.horizontalDirection.opposite)

    override fun getChildren(pos: BlockPos, state: BlockState): Map<BlockPos, BlockState> =
        super.getChildren(pos, state).entries.associate { pos.rotate(state.getValue(FACING).toRotation()) to state }
}