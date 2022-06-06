package com.techarium.techarium.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Determine a region of block
 */
public class BlockRegion {
	// TODO: 06/06/2022 @Ketheroth possibly change this so we can have regions with random shapes instead of only cuboid

	public static BlockRegion FULL_BLOCK = new BlockRegion(1, 1, 1);

	public final int xOffset;
	public final int yOffset;
	public final int zOffset;
	public final int xSize;
	public final int ySize;
	public final int zSize;

	public BlockRegion(int xSize, int ySize, int zSize) {
		this(-xSize / 2, 0, -zSize / 2, xSize, ySize, zSize);
	}

	public BlockRegion(int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
	}

	public VoxelShape toVoxelShape() {
		return Block.box(xOffset * 16, yOffset * 16, zOffset * 16, (xOffset + xSize) * 16, (yOffset + ySize) * 16, (zOffset + zSize) * 16);
	}

}
