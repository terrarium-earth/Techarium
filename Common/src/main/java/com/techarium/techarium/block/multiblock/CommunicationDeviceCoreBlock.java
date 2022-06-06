package com.techarium.techarium.block.multiblock;

import com.techarium.techarium.blockentity.multiblock.CommunicationDeviceCoreBlockEntity;
import com.techarium.techarium.blockentity.multiblock.MultiBlockCoreBlockEntity;
import com.techarium.techarium.registry.TechariumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class CommunicationDeviceCoreBlock extends MultiBlockCoreBlock {

	public CommunicationDeviceCoreBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CommunicationDeviceCoreBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return blockEntityType == TechariumBlockEntities.COMMUNICATION_DEVICE_CORE.get() ? (level1, pos, state1, be) -> ((MultiBlockCoreBlockEntity) be).tick(level1, pos, state1, (MultiBlockCoreBlockEntity) be) : null;
	}

}
