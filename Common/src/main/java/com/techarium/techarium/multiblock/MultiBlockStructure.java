package com.techarium.techarium.multiblock;

import com.techarium.techarium.block.multiblock.MultiBlockCoreBlock;
import com.techarium.techarium.block.multiblock.MultiBlockElementBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import com.techarium.techarium.blockentity.selfdeploying.SelfDeployingMultiBlockBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define a multiblock structure.
 * Positions of the elements are considered offset from the core position, with the core oriented toward south, and in the minecraft axis (south:+z, east:+x).
 */
public class MultiBlockStructure {

	private final Map<BlockPos, MultiBlockElementBlock> positions;
	private MultiBlockCoreBlock core;
	private SelfDeployingBlock selfDeployingBlock;
	private String id;

	private MultiBlockStructure() {
		this.positions = new HashMap<>();
		this.core = null;
		this.id = "";
	}

	/**
	 * @return the id of this structure.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Determine if a valid structure is present in the world at the core position.
	 * The direction should be the direction of the core.
	 *
	 * @param core      the position of the core
	 * @param direction the direction to check the structure
	 * @param level     the level to search for the structure.
	 * @return true if there is a valid structure in the level.
	 */
	public boolean isValidStructure(BlockPos core, Direction direction, Level level) {
		if (!this.core.equals(level.getBlockState(core).getBlock())) {
			// if somehow the core in world doesn't match the multiblock core it's not valid
			return false;
		}
		for (Map.Entry<BlockPos, MultiBlockElementBlock> entry : this.positions.entrySet()) {
			// first rotate the offset, so we're in the same direction as the core block
			BlockPos offset = rotate(entry.getKey(), direction);
			// now we check if the block at this position in the world match.
			BlockPos levelPos = core.offset(offset);
			Block block = level.getBlockState(levelPos).getBlock();
			if (!entry.getValue().equals(block)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Rotate the position from the south direction.
	 *
	 * @param pos       the position to rotate.
	 * @param direction the destination direction of the position.
	 * @return the new position after rotation.
	 */
	private BlockPos rotate(BlockPos pos, Direction direction) {
		// maths are from wikipedia : Rotation_matrix
		return switch (direction) {
			case EAST -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX());  // 270
			case WEST -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX());  // 90
			case NORTH -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());  // 180
			default -> pos;
		};
	}

	/**
	 * Replace the blocks of the structure with the self-deploying blocks.
	 *
	 * @param level   the level.
	 * @param state   the blockstate of the core block.
	 * @param corePos the position of the core block.
	 */
	public void deploy(Level level, BlockState state, BlockPos corePos) {
		if (!this.canDeploy(level, state, corePos)) {
			return;
		}
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		for (BlockPos pos : this.positions.keySet()) {
			// replace multiblock with air so the self-deploying block can safely replace them.
			BlockPos levelPos = corePos.offset(this.rotate(pos, direction));
			level.setBlock(levelPos, Blocks.AIR.defaultBlockState(), 3);
		}
		level.setBlock(corePos, this.selfDeployingBlock.defaultBlockState(), 3);
		if (level.getBlockEntity(corePos) instanceof SelfDeployingMultiBlockBlockEntity selfDeployingBlockEntity) {
			selfDeployingBlockEntity.deploy();
			selfDeployingBlockEntity.setLinkedMultiBlock(this);
		}
	}

	/**
	 * Place the blocks of this structure in the world.
	 * This should be called from a self-deploying block being destructed.
	 *
	 * @param level      the world
	 * @param state      the state of the core block of the self-deploying block
	 * @param corePos    the position of the core block of the self-deployed block
	 * @param initiator  the position of the block that initiated the removal of the self-deployed block
	 * @param dropBlocks determine if the blocks of this multiblock should be dropped in the world instead of placed in the world.
	 */
	public void undeploy(Level level, BlockState state, BlockPos corePos, BlockPos initiator, boolean dropBlocks) {
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		for (Map.Entry<BlockPos, MultiBlockElementBlock> entry : this.positions.entrySet()) {
			BlockPos pos = corePos.offset(this.rotate(entry.getKey(), direction));
			level.setBlock(pos, entry.getValue().defaultBlockState(), 3);
			if (dropBlocks) {
				level.destroyBlock(pos, true);
			}
		}
		level.setBlock(corePos, this.core.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), 3);
		if (dropBlocks) {
			level.destroyBlock(corePos, true);
		}
		level.destroyBlock(initiator, true);
	}

	/**
	 * Determine if the structure can be replaced by the deploying block and it can be deployed.
	 *
	 * @param level   the level of the multiblock.
	 * @param state   the blockstate of the multiblock core.
	 * @param corePos the position of the multiblock core.
	 * @return true if the structure can be deployed.
	 */
	public boolean canDeploy(Level level, BlockState state, BlockPos corePos) {
		return this.selfDeployingBlock.canBePlaced(level, corePos);
	}

	/**
	 * @param level the level of the multiblock.
	 * @param pos   the position of the multiblock core.
	 * @return a list of the blocks that obstruct the self-deploying block to be deployed.
	 */
	public List<BlockPos> getObstructingBlocks(Level level, BlockPos pos) {
		return this.selfDeployingBlock.getObstructingBlocks(level, pos);
	}

	public static class Builder {

		private final MultiBlockStructure structure = new MultiBlockStructure();

		public MultiBlockStructure.Builder setId(String id) {
			this.structure.id = id;
			return this;
		}

		public MultiBlockStructure.Builder setCore(MultiBlockCoreBlock core) {
			this.structure.core = core;
			return this;
		}

		public MultiBlockStructure.Builder setSelfDeployingBlock(SelfDeployingBlock selfDeployingBlock) {
			this.structure.selfDeployingBlock = selfDeployingBlock;
			return this;
		}

		public MultiBlockStructure.Builder addElement(BlockPos position, MultiBlockElementBlock element) {
			this.structure.positions.put(position, element);
			return this;
		}

		public MultiBlockStructure.Builder addElements(BlockPos position, MultiBlockElementBlock element, Object... positionsAndElements) {
			this.structure.positions.put(position, element);
			for (int i = 0; i < positionsAndElements.length; i += 2) {
				this.structure.positions.put((BlockPos) positionsAndElements[i], (MultiBlockElementBlock) positionsAndElements[i + 1]);
			}
			return this;
		}

		public MultiBlockStructure build() {
			return this.structure;
		}

	}

}

