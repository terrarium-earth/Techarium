package com.techarium.techarium.block.entity.selfdeploying;

import com.techarium.techarium.multiblock.MultiblockStructure;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A self-deploying block formed by a multiblock and convert back to it on destroyed.
 */
public abstract class SelfDeployingMultiBlockBlockEntity extends SelfDeployingBlockEntity.WithContainer {

	private MultiblockStructure multiblock;

	public SelfDeployingMultiBlockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void undeploy(boolean removeSelf, boolean restoreMultiBlock, BlockState oldState, BlockPos initiator) {
		super.undeploy(removeSelf, restoreMultiBlock, oldState, initiator);

		if (multiblock != null) {
			multiblock.revert(level, oldState, worldPosition, initiator, !restoreMultiBlock);
		}
	}

	public void setDeployedFrom(MultiblockStructure multiblock) {
		this.multiblock = multiblock;
	}

}
