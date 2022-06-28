package com.techarium.techarium.blockentity.multiblock;

import com.techarium.techarium.block.multiblock.MultiBlockCoreBlock;
import com.techarium.techarium.multiblock.MultiBlockStructure;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * A BlockEntity for the {@link MultiBlockCoreBlock}.
 */
public abstract class MultiBlockCoreBlockEntity extends BlockEntity {

	// TODO @anyone: 12/06/2022 change this to allow displaying for just a few seconds (see TODO.md)
	/**
	 * List of multiblock cores that are obstructed and can't deploy. This is used to render the obstruction overlays.
	 */
	public static List<BlockPos> CORE_WITH_OBSTRUCTION = new ArrayList<>();

	private MultiBlockStructure multiblock;

	public MultiBlockCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.multiblock = null;  // can't retrieve the multiblock because the level isn't set yet.
	}

	public abstract ResourceLocation getMultiBlockStructureId();

	public void onActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
		if (this.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
			if (this.multiblock.canConvert(level, state, pos)) {
				this.multiblock.convert(level, state, pos);
			}
			if (!CORE_WITH_OBSTRUCTION.contains(pos)) {
				CORE_WITH_OBSTRUCTION.add(pos);
			}
		}
	}

	public void tick(Level level, BlockPos pos, BlockState state, MultiBlockCoreBlockEntity tile) {
		if (this.multiblock == null) {
			// now that the level is available, set the multiblock.
			this.multiblock = CommonServices.REGISTRY.getMultiBlockStructure(level, this.getMultiBlockStructureId());
		}

		if (level.getGameTime() % 5 == 0) {
			if (this.multiblock != MultiBlockStructure.EMPTY) {
				if (tile.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
					if (this.multiblock.canConvert(level, state, pos)) {
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
	}

	public List<BlockPos> getObstructingBlocks(Level level, BlockPos pos) {
		if (this.multiblock == null) {
			return List.of();
		}
		return this.multiblock.getObstructingBlocks(level, pos);
	}

}
