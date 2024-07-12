package earth.terrarium.techarium.common.blocks.machines.deploying

import com.mojang.serialization.MapCodec
import earth.terrarium.techarium.common.blocks.entities.machines.BasicDeployChildBlockEntity
import earth.terrarium.techarium.common.registries.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class BasicDeployChildBlock(properties: Properties) : BaseEntityBlock(properties) {
    companion object {
        val CODEC: MapCodec<BasicDeployChildBlock> = simpleCodec(::BasicDeployChildBlock)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity =
        BasicDeployChildBlockEntity(ModBlockEntityTypes.basicDeployChildBlockEntity, pos, state)

    override fun destroy(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        val entity = level.getBlockEntity(pos)
        if (entity is BasicDeployChildBlockEntity ) {
            level.getBlockState(entity.parentPos).block.destroy(level, pos, state)
        }
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val entity = level.getBlockEntity(pos)
        if (entity is BasicDeployChildBlockEntity ) {
            return level.getBlockState(entity.parentPos).useWithoutItem(level, player, hitResult)
        }
        return InteractionResult.PASS
    }
}