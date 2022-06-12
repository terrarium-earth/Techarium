package com.techarium.techarium.blockentity.multiblock;

import com.techarium.techarium.multiblock.MultiBlockStructure;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class CommunicationDeviceCoreBlockEntity extends MultiBlockCoreBlockEntity {

	public CommunicationDeviceCoreBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.COMMUNICATION_DEVICE_CORE.get(), pos, state);
	}

	@Override
	public Supplier<MultiBlockStructure> getDefaultMultiBlockStructure() {
		return () -> new MultiBlockStructure.Builder()
				.setId("com_device")
				.setSelfDeployingBlock(TechariumBlocks.EXCHANGE_STATION.get())
				.setCore(TechariumBlocks.COMMUNICATION_DEVICE_CORE.get())
				.addElement(new BlockPos(0, 1, 0), TechariumBlocks.COMMUNICATION_DEVICE_ELEMENT.get())
				.build();
	}

}
