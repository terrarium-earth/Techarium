package earth.terrarium.techarium.common.blocks.machines.farming

import com.mojang.serialization.MapCodec
import earth.terrarium.techarium.common.blocks.base.MachineBlock
import earth.terrarium.techarium.common.blocks.base.Tickable
import earth.terrarium.techarium.common.blocks.entities.machines.SprinklerBlockEntity
import earth.terrarium.techarium.common.registries.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.fluids.FluidUtil

class SprinklerBlock(properties: Properties) : MachineBlock(properties) {

    override val deploymentTime: Int = 30

    override fun codec(): MapCodec<SprinklerBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = SprinklerBlockEntity(pos, state)

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        SHAPE

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.direction)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
    }

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        state: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return Tickable.createTicker(blockEntityType, ModBlockEntityTypes.sprinkler)
    }

    companion object {
        private val CODEC = Block.simpleCodec(::SprinklerBlock)
        private val SHAPE = Block.box(
            4.0, 0.0, 4.0,
            12.0, 8.0, 12.0
        )
    }
}