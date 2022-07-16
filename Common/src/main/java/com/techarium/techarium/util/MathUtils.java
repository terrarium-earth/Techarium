package com.techarium.techarium.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class MathUtils {

	/**
	 * Rotate the position from the south direction to the given direction.
	 * The center of the rotation is considered (0,0,0) as the position should be an offset and not the real world position.
	 *
	 * @param pos       the position to rotate.
	 * @param direction the destination direction of the position.
	 * @return the new position after rotation.
	 */
	public static BlockPos rotate(BlockPos pos, Direction direction) {
		// maths are from wikipedia : Rotation_matrix
		return switch (direction) {
			case EAST -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX());  // 270
			case WEST -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX());  // 90
			case NORTH -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ());  // 180
			default -> pos;
		};
	}
}
