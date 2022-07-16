package com.techarium.techarium.client.model;

import com.techarium.techarium.block.entity.selfdeploying.ExchangeStationBlockEntity;
import com.techarium.techarium.util.Utils;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ExchangeStationModel extends AnimatedGeoModel<ExchangeStationBlockEntity> {

	public static final ResourceLocation MODEL = Utils.resourceLocation("geo/exchange_station.geo.json");
	public static final ResourceLocation TEXTURE = Utils.resourceLocation("textures/block/animated/exchange_station.png");
	public static final ResourceLocation ANIMATION = Utils.resourceLocation("animations/exchange_station.animation.json");

	@Override
	public ResourceLocation getModelResource(ExchangeStationBlockEntity object) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(ExchangeStationBlockEntity object) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(ExchangeStationBlockEntity animatable) {
		return ANIMATION;
	}

}
