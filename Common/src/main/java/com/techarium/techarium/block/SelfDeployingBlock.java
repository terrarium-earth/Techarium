package com.techarium.techarium.block;

import com.techarium.techarium.blockentity.SelfDeployingBlockEntity;
import com.techarium.techarium.multiblock.MultiBlockBaseCore;
import com.techarium.techarium.multiblock.MultiBlockBaseElement;
import com.techarium.techarium.util.BlockRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A class for blocks that are deployed after placed in the world.
 * See {@link SelfDeployingBlockEntity} for the block entity associated. This block entity take care of the deployment.
 * See {@link SelfDeployingSlaveBlock} for the block used by this block to maintain its state in the world and "claim" the positions.
 */
public abstract class SelfDeployingBlock extends Block implements EntityBlock {

	public SelfDeployingBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.getBlockSize().toVoxelShape();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction dir = context.getPlayer().isShiftKeyDown() ? context.getHorizontalDirection() : context.getHorizontalDirection().getOpposite();
		return super.getStateForPlacement(context).setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			selfDeployingBlockEntity.deploy();
		}
	}

	@Override
	public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (oldState.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
				selfDeployingBlockEntity.undeploy();
			}
		}
		super.onRemove(oldState, level, pos, newState, isMoving);
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			return selfDeployingBlockEntity.onUse(player, hand);
		}
		return InteractionResult.PASS;
	}

	/**
	 * Determine if the block can be placed according to its size after deployment.
	 *
	 * @param world the world.
	 * @param pos   the position of the block.
	 * @return true if the block can be placed.
	 */
	public boolean canBePlaced(Level world, BlockPos pos) {
		// TODO: 06/06/2022 @Ketheroth find a proper way to do that. add orientation according the core direction. (move to MultiBlockStructure ?)
		BlockRegion region = getBlockSize();
		for (int x = region.xOffset; x < region.xSize - region.xOffset; x++) {
			for (int y = region.yOffset; y < region.ySize - region.yOffset; y++) {
				for (int z = region.zOffset; z < region.zSize - region.zOffset; z++) {
					BlockState state = world.getBlockState(pos.offset(x, y, z));
					if (!(state.getMaterial().isReplaceable() || state.getBlock() instanceof MultiBlockBaseCore || state.getBlock() instanceof MultiBlockBaseElement)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * @return the size of the block after deployment.
	 */
	public BlockRegion getBlockSize() {
		return BlockRegion.FULL_BLOCK;
	}

}
