package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A child block used to fill the air in the world. It will proxy received calls to its father.<br>
 * See {@link SelfDeployingBlockEntity}, the father block entity.
 */
public class SelfDeployingChildBlockEntity extends BlockEntity {

	private BlockPos parentPosition;

	public SelfDeployingChildBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.SELF_DEPLOYING_CHILD.get(), pos, state);
		this.parentPosition = BlockPos.ZERO;
	}

	public void setParentPosition(BlockPos position) {
		this.parentPosition = position;
	}

	/**
	 * When the child block is used, proxy the call to the block at father position.
	 *
	 * @return the result of the interaction with the father block
	 */
	public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity blockEntity = level.getBlockEntity(this.parentPosition);
		if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			return selfDeployingBlockEntity.onUse(player, hand);
		}
		return InteractionResult.SUCCESS;
	}

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		if (level.getBlockEntity(this.parentPosition) instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			// default : the machine is removed and if it was from a multiblock the multiblock is restored
			selfDeployingBlockEntity.undeploy(true, !(stack.is(TechariumItems.TECH_TOOL.get()) || player.isShiftKeyDown()), level.getBlockState(this.parentPosition), pos);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		CompoundTag pos = new CompoundTag();
		tag.put("father", NbtUtils.writeBlockPos(this.parentPosition));
	}

	@Override
	public void load(CompoundTag tag) {
		this.parentPosition = NbtUtils.readBlockPos(tag.getCompound("father"));
	}

}
