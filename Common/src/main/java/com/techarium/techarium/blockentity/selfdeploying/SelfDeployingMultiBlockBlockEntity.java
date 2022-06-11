package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.multiblock.MultiBlockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A self-deploying block formed by a multiblock and convert back to it on destroyed.
 */
public abstract class SelfDeployingMultiBlockBlockEntity extends SelfDeployingBlockEntity {

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

}