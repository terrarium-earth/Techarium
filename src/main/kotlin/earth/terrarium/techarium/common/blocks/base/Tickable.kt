package earth.terrarium.techarium.common.blocks.base

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

interface Tickable {

    fun tick(level: Level, pos: BlockPos, state: BlockState) {
        if (level.isClientSide) {
            this.clientTick(level, pos, state)
        } else if (level is ServerLevel) {
            this.serverTick(level, pos, state)
        }
    }

    fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState) {}

    fun clientTick(level: Level, pos: BlockPos, state: BlockState) {}

    companion object {

        fun <I : BlockEntity?, O> createTicker(
            input: BlockEntityType<I>,
            output: BlockEntityType<O>
        ): BlockEntityTicker<I>? where O : BlockEntity, O : Tickable {
            if (input == output) {
                return BlockEntityTicker { level, pos, state, blockEntity ->
                    @Suppress("UNCHECKED_CAST")
                    (blockEntity as O).tick(level, pos, state)
                }
            }
            return null
        }
    }
}