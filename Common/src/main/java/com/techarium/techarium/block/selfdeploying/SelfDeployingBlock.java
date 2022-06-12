package com.techarium.techarium.block.selfdeploying;

import com.techarium.techarium.block.multiblock.MultiBlockCoreBlock;
import com.techarium.techarium.block.multiblock.MultiBlockElementBlock;
import com.techarium.techarium.blockentity.selfdeploying.SelfDeployingBlockEntity;
import com.techarium.techarium.registry.TechariumItems;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A class for blocks that are deployed after placed in the world.<br>
 * See {@link SelfDeployingBlockEntity} for the block entity associated. This block entity take care of the deployment.<br>
 * See {@link SelfDeployingSlaveBlock} for the block used by this block to maintain its state in the world and "claim" the positions.<br>
 * <br>
 * Child classes should override {@link SelfDeployingBlock#getDeployedSize()} to change the size of the deployed block. Default is (1,1,1).
 */
public abstract class SelfDeployingBlock extends Block implements EntityBlock {

	public SelfDeployingBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.getDeployedSize().toVoxelShape();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
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
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(level, player, pos, state, blockEntity, stack);
		if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			// default : the machine is removed and if it was from a multiblock the multiblock is restored
			selfDeployingBlockEntity.undeploy(false, !(stack.is(TechariumItems.TECH_TOOL.get()) || player.isShiftKeyDown()), state, pos);
		}
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
		// TODO @Ketheroth: 06/06/2022 find a proper way to do that. add orientation according the core direction. (move to MultiBlockStructure ?)
		BlockRegion region = this.getDeployedSize();
		for (int x = region.xOffset; x < region.xSize - region.xOffset; x++) {
			for (int y = region.yOffset; y < region.ySize - region.yOffset; y++) {
				for (int z = region.zOffset; z < region.zSize - region.zOffset; z++) {
					BlockState state = world.getBlockState(pos.offset(x, y, z));
					if (!(state.getMaterial().isReplaceable() || state.getBlock() instanceof MultiBlockCoreBlock || state.getBlock() instanceof MultiBlockElementBlock)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * @param level the level of the multiblock.
	 * @param pos   the position of the multiblock core.
	 * @return a list of the blocks that obstruct the self-deploying block to be deployed.
	 */
	public List<BlockPos> getObstructingBlocks(Level level, BlockPos pos) {
		List<BlockPos> positions = new ArrayList<>();
		BlockRegion region = this.getDeployedSize();
		for (int x = region.xOffset; x < region.xSize - region.xOffset; x++) {
			for (int y = region.yOffset; y < region.ySize - region.yOffset; y++) {
				for (int z = region.zOffset; z < region.zSize - region.zOffset; z++) {
					BlockPos offset = pos.offset(x, y, z);
					BlockState state = level.getBlockState(offset);
					if (!(state.getMaterial().isReplaceable() || state.getBlock() instanceof MultiBlockCoreBlock || state.getBlock() instanceof MultiBlockElementBlock)) {
						positions.add(offset);
					}
				}
			}
		}
		return positions;
	}

	/**
	 * @return the size of the block after deployment.
	 */
	public BlockRegion getDeployedSize() {
		return BlockRegion.FULL_BLOCK;
	}

}
