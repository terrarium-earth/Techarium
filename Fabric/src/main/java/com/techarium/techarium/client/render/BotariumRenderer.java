package com.techarium.techarium.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.techarium.techarium.blockentity.selfdeploying.BotariumBlockEntity;
import com.techarium.techarium.blockentity.selfdeploying.ExchangeStationBlockEntity;
import com.techarium.techarium.client.model.BotariumModel;
import com.techarium.techarium.client.model.ExchangeStationModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class BotariumRenderer extends GeoBlockRenderer<BotariumBlockEntity>
{
	public BotariumRenderer() {
		super(new BotariumModel());
	}

	@Override
	public RenderType getRenderType(BotariumBlockEntity animatable, float partialTicks, PoseStack stack,
									@Nullable MultiBufferSource renderTypeBuffer,
									@Nullable VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
	}

}
