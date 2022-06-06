package com.techarium.techarium.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.techarium.techarium.blockentity.ExchangeStationBlockEntity;
import com.techarium.techarium.client.model.ExchangeStationModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class ExchangeStationRenderer extends GeoBlockRenderer<ExchangeStationBlockEntity>
{
	public ExchangeStationRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
		super(rendererDispatcherIn, new ExchangeStationModel());
	}


	@Override
	public RenderType getRenderType(ExchangeStationBlockEntity animatable, float partialTicks, PoseStack stack,
									@Nullable MultiBufferSource renderTypeBuffer,
									@Nullable VertexConsumer vertexBuilder, int packedLightIn,
									ResourceLocation textureLocation) {
//		return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
		return RenderType.entityCutoutNoCullZOffset(this.getTextureLocation(animatable));
	}

}
