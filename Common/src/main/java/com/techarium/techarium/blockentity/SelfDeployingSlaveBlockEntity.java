package com.techarium.techarium.blockentity;

import com.techarium.techarium.registry.TechariumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A slave block used to fill the air in the world. It will proxy received calls to its master.
 * See {@link com.techarium.techarium.blockentity.SelfDeployingSlaveBlockEntity}, the master block entity
 */
public class SelfDeployingSlaveBlockEntity extends BlockEntity {

	private BlockPos masterPosition = new BlockPos(0, 0, 0);

	public SelfDeployingSlaveBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.SELF_DEPLOYING_SLAVE.get(), pos, state);
	}

	public void setMasterPosition(BlockPos position) {
		this.masterPosition = position;
	}

	/**
	 * When the slave block is used, proxy the call to the block at master position.
	 * @return the result of the interaction with the master block
	 */
	public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			// we do nothing in the client cause the master position is not present
			return InteractionResult.SUCCESS;
		}
		BlockEntity blockEntity = level.getBlockEntity(this.masterPosition);
		if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			return selfDeployingBlockEntity.onUse(player, hand);
		}
		return InteractionResult.PASS;
	}

	public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		BlockEntity blockEntity = level.getBlockEntity(this.masterPosition);
		if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			selfDeployingBlockEntity.undeploy();
		}
	}

}
