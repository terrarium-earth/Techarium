package com.techarium.techarium.block.multiblock;

import com.techarium.techarium.blockentity.multiblock.MultiBlockCoreBlockEntity;
import com.techarium.techarium.blockentity.multiblock.TestStationCoreBlockEntity;
import com.techarium.techarium.registry.TechariumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class TestStationCoreBlock extends MultiBlockCoreBlock {

	public TestStationCoreBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TestStationCoreBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return  blockEntityType == TechariumBlockEntities.TEST_STATION_CORE.get() ? (level1, pos, state1, be) -> ((MultiBlockCoreBlockEntity) be).tick(level1, pos, state1, (MultiBlockCoreBlockEntity) be) : null;
	}

}
