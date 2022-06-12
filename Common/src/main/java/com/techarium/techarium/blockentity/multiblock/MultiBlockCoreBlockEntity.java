package com.techarium.techarium.blockentity.multiblock;

import com.techarium.techarium.block.multiblock.MultiBlockCoreBlock;
import com.techarium.techarium.multiblock.MultiBlockRegistry;
import com.techarium.techarium.multiblock.MultiBlockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A BlockEntity for the {@link MultiBlockCoreBlock}.
 */
public abstract class MultiBlockCoreBlockEntity extends BlockEntity {

	// TODO: 12/06/2022 change this to allow displaying for just a few seconds (see TODO.md)
	public static List<BlockPos> CORE_WITH_OBSTRUCTION = new ArrayList<>();

	private MultiBlockStructure multiblock;

	public MultiBlockCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.multiblock = MultiBlockRegistry.getOrSet(this.getMultiBlockStructureId(), this.getDefaultMultiBlockStructure());
	}

	public abstract String getMultiBlockStructureId();

	public abstract Supplier<MultiBlockStructure> getDefaultMultiBlockStructure();

	public void onActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
		if (this.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
			if (this.multiblock.canDeploy(level, state, pos)) {
				this.multiblock.deploy(level, state, pos);
			}
			if (!CORE_WITH_OBSTRUCTION.contains(pos)) {
				CORE_WITH_OBSTRUCTION.add(pos);
			}
		}
	}

	public void tick(Level level, BlockPos pos, BlockState state, MultiBlockCoreBlockEntity tile) {
		if (level.getGameTime() % 5 == 0) {
			if (tile.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
				if (this.multiblock.canDeploy(level, state, pos)) {
					CORE_WITH_OBSTRUCTION.remove(pos);
					level.setBlock(pos, state.setValue(MultiBlockCoreBlock.READY, true), 3);
				} else {
					if (!CORE_WITH_OBSTRUCTION.contains(pos)) {
						CORE_WITH_OBSTRUCTION.add(pos);
					}
				}
			} else {
				level.setBlock(pos, state.setValue(MultiBlockCoreBlock.READY, false), 3);
			}
		}
	}

	public List<BlockPos> getObstructingBlocks(Level level, BlockPos pos) {
		return this.multiblock.getObstructingBlocks(level, pos);
	}

}
