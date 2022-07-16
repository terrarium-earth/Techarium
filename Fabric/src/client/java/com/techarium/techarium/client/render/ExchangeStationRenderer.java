package com.techarium.techarium.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.techarium.techarium.block.entity.selfdeploying.ExchangeStationBlockEntity;
import com.techarium.techarium.client.model.ExchangeStationModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class ExchangeStationRenderer extends GeoBlockRenderer<ExchangeStationBlockEntity> {

	public ExchangeStationRenderer() {
		super(new ExchangeStationModel());
	}

	@Override
	public RenderType getRenderType(ExchangeStationBlockEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
	}

}
