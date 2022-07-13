package com.techarium.techarium.fabric.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.techarium.techarium.blockentity.selfdeploying.BotariumBlockEntity;
import com.techarium.techarium.client.model.BotariumModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;


public class BotariumRenderer extends GeoBlockRenderer<BotariumBlockEntity> {

	public BotariumRenderer() {
		super(new BotariumModel());
	}

	@Override
	public RenderType getRenderType(BotariumBlockEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
	}

}
