package com.techarium.techarium.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Define a multiblock structure.
 * Positions of the elements are considered offset from the core position, with the core oriented toward south, and in the minecraft axis (south:+z, east:+x).
 */
public class MultiBlockStructure {

	private final Map<BlockPos, MultiBlockBaseElement> positions;
	private MultiBlockBaseCore core;
	private Block selfDeployingBlock;// TODO @Ketheroth: 04/06/2022 refactor code so we can have the class something like SelfDeployingBlock

	// TODO @Ketheroth: 04/06/2022 add a way to reform the structure after the self-deployed block is destroyed ??

	private MultiBlockStructure() {
		this.positions = new HashMap<>();
		this.core = null;
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
		for (Map.Entry<BlockPos, MultiBlockBaseElement> entry : this.positions.entrySet()) {
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
		System.out.println("deploying");
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		for (BlockPos pos : this.positions.keySet()) {
			BlockPos levelPos = corePos.offset(rotate(pos, direction));
			level.setBlock(levelPos, Blocks.AIR.defaultBlockState(), 3);
		}
		level.setBlock(corePos, this.selfDeployingBlock.defaultBlockState(), 3);
//		if (level.getBlockEntity(corePos) instanceof DeployMasterTile<?> master) {
//			master.placeSlaves();
//			 TODO: 04/06/2022 @Ketheroth when there is a good way to have a SelfDeployingBlockEntity use that instead
//			System.out.println("placing slaves");
//		} else {
//			System.out.println("smtg went wrong " + level.getBlockEntity(corePos).getClass());
//		}
	}

	public static class Builder {

		private final MultiBlockStructure structure = new MultiBlockStructure();

		public MultiBlockStructure.Builder setCore(MultiBlockBaseCore core) {
			this.structure.core = core;
			return this;
		}

		public MultiBlockStructure.Builder setSelfDeployingBlock(Block selfDeployingBlock) {
			this.structure.selfDeployingBlock = selfDeployingBlock;
			return this;
		}

		public MultiBlockStructure.Builder addElement(BlockPos position, MultiBlockBaseElement element) {
			this.structure.positions.put(position, element);
			return this;
		}

		public MultiBlockStructure.Builder addElements(BlockPos position, MultiBlockBaseElement element, Object... positionsAndElements) {
			this.structure.positions.put(position, element);
			for (int i = 0; i < positionsAndElements.length; i += 2) {
				this.structure.positions.put((BlockPos) positionsAndElements[i], (MultiBlockBaseElement) positionsAndElements[i + 1]);
			}
			return this;
		}

		public MultiBlockStructure build() {
			return this.structure;
		}

	}

}

