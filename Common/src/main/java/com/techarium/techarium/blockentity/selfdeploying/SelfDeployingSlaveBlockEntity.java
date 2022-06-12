package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A slave block used to fill the air in the world. It will proxy received calls to its master.<br>
 * See {@link SelfDeployingSlaveBlockEntity}, the master block entity.
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
	 *
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

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		if (level.getBlockEntity(this.masterPosition) instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			// default : the machine is removed and if it was from a multiblock the multiblock is restored
			selfDeployingBlockEntity.undeploy(true, !(stack.is(TechariumItems.TECH_TOOL.get()) || player.isShiftKeyDown()), level.getBlockState(this.masterPosition), pos);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		CompoundTag pos = new CompoundTag();
		pos.putInt("x", this.masterPosition.getX());
		pos.putInt("y", this.masterPosition.getY());
		pos.putInt("z", this.masterPosition.getZ());
		tag.put("master", pos);
	}

	@Override
	public void load(CompoundTag tag) {
		CompoundTag master = (CompoundTag) tag.get("master");
		this.masterPosition = new BlockPos(
				master.getInt("x"),
				master.getInt("y"),
				master.getInt("z")
		);
	}

}
