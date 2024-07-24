package earth.terrarium.techarium.common.blocks.machines.farming

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.state.BlockState

class StableFarmBlock(properties: Properties) : FarmBlock(properties) {

    init {
        registerDefaultState(defaultBlockState().setValue(MOISTURE, 7))
    }

    override fun fallOn(level: Level, state: BlockState, pos: BlockPos, entity: Entity, fallDistance: Float) {
        entity.causeFallDamage(fallDistance, 1.0f, entity.damageSources().fall())
    }

    override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val moisture = state.getValue(MOISTURE)
        if (moisture != 0) {
            level.setBlock(pos, state.setValue(MOISTURE, moisture - 1), Block.UPDATE_CLIENTS)
        } else {
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState().setValue(MOISTURE, 6), Block.UPDATE_CLIENTS)
        }
    }
}