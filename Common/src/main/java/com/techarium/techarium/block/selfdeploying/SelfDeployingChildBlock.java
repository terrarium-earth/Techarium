package com.techarium.techarium.block.selfdeploying;

import com.techarium.techarium.blockentity.selfdeploying.SelfDeployingChildBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A simple black used to fill the air and proxy its call to its parent.
 */
public class SelfDeployingChildBlock extends Block implements EntityBlock {

	public SelfDeployingChildBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof SelfDeployingChildBlockEntity selfDeployingChildBlockEntity) {
			return selfDeployingChildBlockEntity.onUse(state, level, pos, player, hand, hit);
		}
		return InteractionResult.PASS;
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, blockEntity, stack);
		if (blockEntity instanceof SelfDeployingChildBlockEntity selfDeployingChildBlockEntity) {
			selfDeployingChildBlockEntity.playerDestroy(level, player, pos, state, blockEntity, stack);
		}
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SelfDeployingChildBlockEntity(pos, state);
	}

	@Override
	protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
		// we don't want particles
	}

}
