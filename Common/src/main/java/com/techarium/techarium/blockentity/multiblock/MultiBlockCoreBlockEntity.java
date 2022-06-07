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

import java.util.function.Supplier;

/**
 * A BlockEntity for the {@link MultiBlockCoreBlock}.
 */
public abstract class MultiBlockCoreBlockEntity extends BlockEntity {

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
		}
	}

	public void tick(Level level, BlockPos pos, BlockState state, MultiBlockCoreBlockEntity tile) {
		if (level.getGameTime() % 5 == 0) {
			if (tile.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
				if (this.multiblock.canDeploy(level, state, pos)) {
					level.setBlock(pos, state.setValue(MultiBlockCoreBlock.READY, true), 3);
				} else {
					if (level.isClientSide) {
						this.multiblock.showObstructingBlocks(level, pos);
						// TODO @Ketheroth: 06/06/2022 once this works, make it display only for a few seconds and then show again when block is right-clicked
					}
				}
			} else {
				level.setBlock(pos, state.setValue(MultiBlockCoreBlock.READY, false), 3);
			}
		}
	}

}
