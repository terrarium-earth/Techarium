package com.techarium.techarium.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Supplier;

/**
 * A BlockEntity for the {@link MultiBlockBaseCore}
 */
public abstract class MultiBlockBaseTile extends BlockEntity {

	private MultiBlockStructure multiblock;

	public MultiBlockBaseTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.multiblock = MultiBlockRegistry.getOrSet(this.getMultiBlockStructureId(), this.getDefaultMultiBlockStructure());
	}

	public abstract String getMultiBlockStructureId();

	public abstract Supplier<MultiBlockStructure> getDefaultMultiBlockStructure();

	public void onActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
		if (this.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
			this.multiblock.deploy(level, state, pos);
		}
	}

	public void tick(Level level, BlockPos pos, BlockState state, MultiBlockBaseTile tile) {
		if (level.getGameTime()%5==0) {
			if (tile.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
				level.setBlock(pos, state.setValue(MultiBlockBaseCore.READY, true), 3);
			} else {
				level.setBlock(pos, state.setValue(MultiBlockBaseCore.READY, false), 3);
			}
		}
	}

}
