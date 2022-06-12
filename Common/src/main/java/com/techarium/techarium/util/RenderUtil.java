package com.techarium.techarium.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RenderUtil {

	/**
	 * Display a red outline around a block.
	 *
	 * @param x the x position of the block.
	 * @param y the y position of the block.
	 * @param z the z position of the block.
	 */
	public static void displayRedOutline(PoseStack poseStack, double x, double y, double z) {
		Vec3 view = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		VoxelShape shape = Shapes.block();
		LevelRenderer.renderShape(poseStack,
				Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.LINES),
				shape,
				x - view.x, y - view.y, z - view.z,
				1, 0, 0, 0.5F);
		Minecraft.getInstance().renderBuffers().bufferSource().endBatch(RenderType.LINES);
	}

}
