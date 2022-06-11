package com.techarium.techarium.block.selfdeploying;

import com.techarium.techarium.blockentity.selfdeploying.ExchangeStationBlockEntity;
import com.techarium.techarium.util.BlockRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class ExchangeStationBlock extends SelfDeployingBlock {

	public ExchangeStationBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ExchangeStationBlockEntity(pos, state);
	}

//	@Nullable
//	@Override
//	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
//		// implement tick if useful
//		return null;
//	}

	@Override
	public BlockRegion getDeployedSize() {
		return new BlockRegion(1, 3, 1);// TODO @Ketheroth: 07/06/2022 replace 3 by 2 when the tests are ok
	}

}