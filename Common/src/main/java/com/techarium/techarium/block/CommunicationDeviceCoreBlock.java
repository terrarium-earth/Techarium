package com.techarium.techarium.block;

import com.techarium.techarium.blockentity.CommunicationDeviceCoreBlockEntity;
import com.techarium.techarium.multiblock.MultiBlockBaseCore;
import com.techarium.techarium.multiblock.MultiBlockBaseTile;
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

public class CommunicationDeviceCoreBlock extends MultiBlockBaseCore {

	public CommunicationDeviceCoreBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CommunicationDeviceCoreBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return blockEntityType == TechariumBlockEntities.COMMUNICATION_DEVICE_CORE.get() ? (level1, pos, state1, be) -> ((MultiBlockBaseTile) be).tick(level1, pos, state1, (MultiBlockBaseTile) be) : null;
	}

}
