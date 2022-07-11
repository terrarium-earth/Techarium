package com.techarium.techarium.block.multiblock;

import com.techarium.techarium.blockentity.multiblock.MachineCoreBlockEntity;
import com.techarium.techarium.registry.TechariumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * A Core Block for a multiblock structure.
 */
public class MachineCoreBlock extends Block implements EntityBlock {

	/**
	 * 0 : multiblock not selected (blue)
	 * 1 : multiblock selected but not valid (red)
	 * 2 : multiblock selected and valid (green)
	 */
	public static IntegerProperty READY = IntegerProperty.create("ready", 0, 2);

	public MachineCoreBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL));
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
				.setValue(READY, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING, READY);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction dir = context.getPlayer().isShiftKeyDown() ? context.getHorizontalDirection() : context.getHorizontalDirection().getOpposite();
		return super.getStateForPlacement(context).setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide && player instanceof ServerPlayer) {
			BlockEntity be = level.getBlockEntity(pos);
			if (be instanceof MachineCoreBlockEntity mbe) {
				return mbe.onActivated(state, level, pos, player, hand);
			}
		}
		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MachineCoreBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return blockEntityType == TechariumBlockEntities.MACHINE_CORE.get() ? (level1, pos, state1, be) -> ((MachineCoreBlockEntity) be).tick(level1, pos, state1, (MachineCoreBlockEntity) be) : null;
	}

}
