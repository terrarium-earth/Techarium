package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.multiblock.MultiBlockStructure;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A self-deploying block formed by a multiblock and convert back to it on destroyed.
 */
public abstract class SelfDeployingMultiBlockBlockEntity extends SelfDeployingBlockEntity.WithModules {

	private MultiBlockStructure linkedMultiBlock;

	public SelfDeployingMultiBlockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void undeploy(boolean removeSelf, boolean restoreMultiBlock, BlockState oldState, BlockPos initiator) {
		super.undeploy(removeSelf, restoreMultiBlock, oldState, initiator);
		this.linkedMultiBlock.undeploy(this.level, oldState, this.worldPosition, initiator, !restoreMultiBlock);
	}

	public void setLinkedMultiBlock(MultiBlockStructure linkedMultiBlock) {
		this.linkedMultiBlock = linkedMultiBlock;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		// TODO @Ketheroth: 17/06/2022 change the way multiblock are registered cause right now, they are registered after the tag are read
		tag.putString("multiblock", this.linkedMultiBlock.getId());
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.linkedMultiBlock = CommonServices.REGISTRY.getMultiBlockStructure(this.level, new ResourceLocation("techarium", tag.getString("multiblock")));
	}

}
