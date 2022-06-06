package com.techarium.techarium.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class RenderUtil {

	/**
	 * Display a red outline around a block.
	 *
	 * @param x the x position of the block.
	 * @param y the y position of the block.
	 * @param z the z position of the block.
	 */
	public static void displayRedOutline(double x, double y, double z) {
		// TODO @SomeoneGoodWithRendering: 06/06/2022 please fix this I (Ketheroth) can't make it works, the outline isn't displayed ;(
		MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer consumer = buffer.getBuffer(RenderType.LINES);
		LevelRenderer.renderLineBox(consumer, x, y, z, x + 1, y + 1, z + 1, 1, 0, 0, 1);
		buffer.endBatch(RenderType.LINES);
	}

}
