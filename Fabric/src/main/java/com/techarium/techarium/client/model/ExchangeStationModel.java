package com.techarium.techarium.client.model;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.blockentity.selfdeploying.ExchangeStationBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ExchangeStationModel extends AnimatedGeoModel<ExchangeStationBlockEntity> {

	@Override
	public ResourceLocation getModelResource(ExchangeStationBlockEntity object) {
		return new ResourceLocation(Techarium.MOD_ID, "geo/exchange_station/exchange_station.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ExchangeStationBlockEntity object) {
		return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/exchange_station.png");
	}

	@Override
	public ResourceLocation getAnimationResource(ExchangeStationBlockEntity animatable) {
		return new ResourceLocation(Techarium.MOD_ID, "animations/exchange_station.animation.json");
	}

}
