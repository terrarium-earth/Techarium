package com.techarium.techarium.blockentity.multiblock;

import com.techarium.techarium.registry.TechariumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class CommunicationDeviceCoreBlockEntity extends MultiBlockCoreBlockEntity {

	public CommunicationDeviceCoreBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.COMMUNICATION_DEVICE_CORE.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiBlockStructureId() {
		return new ResourceLocation("techarium", "exchange_station");
	}

}
