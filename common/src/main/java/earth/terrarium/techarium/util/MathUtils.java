package earth.terrarium.techarium.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

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

	/**
	 * Offset a vector 3 by a specified distance in a specified direction.
	 *
	 * @param position the original position to offset from.
	 * @param direction the direction to offset in.
	 * @param distance the distance to offset.
	 * @return the offset vector3.
	 */
	public static Vec3 vec3FromOffset(Vec3 position, Direction direction, double distance){
		return switch (direction){
			case DOWN -> new Vec3(position.x, position.y - distance, position.z);
			case UP -> new Vec3(position.x, position.y + distance, position.z);
			case NORTH -> new Vec3(position.x, position.y, position.z - distance);
			case SOUTH -> new Vec3(position.x, position.y, position.z + distance);
			case WEST -> new Vec3(position.x - distance, position.y, position.z);
			case EAST -> new Vec3(position.x + distance, position.y, position.z);
		};
	}
}
