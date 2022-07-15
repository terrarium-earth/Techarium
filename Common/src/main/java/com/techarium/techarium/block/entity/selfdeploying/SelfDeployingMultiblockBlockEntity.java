package com.techarium.techarium.block.entity.selfdeploying;

import com.techarium.techarium.multiblock.MultiblockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A self-deploying block formed by a multiblock and convert back to it on destroyed.
 */
public abstract class SelfDeployingMultiblockBlockEntity extends SelfDeployingBlockEntity.WithContainer {

	private MultiblockStructure multiblock;

	public SelfDeployingMultiblockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void undeploy(boolean removeSelf, boolean restoreMultiblock, BlockState oldState, BlockPos initiator) {
		super.undeploy(removeSelf, restoreMultiblock, oldState, initiator);

		if (multiblock != null) {
			multiblock.revert(level, oldState, worldPosition, initiator, !restoreMultiblock);
		}
	}

	public void setDeployedFrom(MultiblockStructure multiblock) {
		this.multiblock = multiblock;
	}

}
