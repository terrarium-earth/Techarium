package earth.terrarium.techarium.common.blocks.base

import earth.terrarium.techarium.common.blocks.base.TechariumBlockStates.DEPLOYED
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED

abstract class MachineBlock(properties: Properties) : BaseEntityBlock(properties) {

    abstract val deploymentTime: Int

    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(POWERED, false)
                .setValue(DEPLOYED, false)
        )

    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(POWERED)
        builder.add(DEPLOYED)
    }

    override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
        super.setPlacedBy(level, pos, state, placer, stack)

        level.scheduleTick(pos, this, deploymentTime)
    }

    final override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        if (state.getValue(DEPLOYED)) return
        level.setBlockAndUpdate(pos, state.setValue(DEPLOYED, true))
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        neighborBlock: Block,
        neighborPos: BlockPos,
        movedByPiston: Boolean
    ) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
        if (level.isClientSide) return
        if (state.isSignalSource) return
        val hasSignal = level.hasNeighborSignal(pos)
        if (state.getValue(POWERED) == hasSignal) return
        level.setBlockAndUpdate(pos, state.setValue(POWERED, hasSignal))
    }

    override fun getRenderShape(state: BlockState): RenderShape {
        return RenderShape.ENTITYBLOCK_ANIMATED
    }
}