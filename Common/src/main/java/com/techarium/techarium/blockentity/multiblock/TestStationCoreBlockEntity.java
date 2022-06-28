package com.techarium.techarium.blockentity.multiblock;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.registry.TechariumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TestStationCoreBlockEntity extends MultiBlockCoreBlockEntity {

	public TestStationCoreBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.TEST_STATION_CORE.get(), pos, state);
	}

	@Override
	public ResourceLocation getMultiBlockStructureId() {
		return Techarium.resourceLocation("test_station");
	}

}
