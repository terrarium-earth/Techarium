package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.platform.CommonServices;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A self-deploying block formed by a multiblock and convert back to it on destroyed.
 */
public abstract class SelfDeployingMultiBlockBlockEntity extends SelfDeployingBlockEntity.WithModules {

	private ResourceLocation multiblockId;

	public SelfDeployingMultiBlockBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void undeploy(boolean removeSelf, boolean restoreMultiBlock, BlockState oldState, BlockPos initiator) {
		super.undeploy(removeSelf, restoreMultiBlock, oldState, initiator);
		CommonServices.REGISTRY.getMultiBlockStructure(this.level, this.multiblockId)
				.ifPresent(multiBlockStructure -> multiBlockStructure.revert(this.level, oldState, this.worldPosition, initiator, !restoreMultiBlock));
	}

	public void setDeployedFrom(ResourceLocation multiblockId) {
		this.multiblockId = multiblockId;
	}

}
