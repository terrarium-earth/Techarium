package com.techarium.techarium.client.model;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.blockentity.selfdeploying.BotariumBlockEntity;
import com.techarium.techarium.blockentity.selfdeploying.ExchangeStationBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BotariumModel extends AnimatedGeoModel<BotariumBlockEntity> {

	@Override
	public ResourceLocation getModelResource(BotariumBlockEntity object) {
		return new ResourceLocation(Techarium.MOD_ID, "geo/botarium.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(BotariumBlockEntity object) {
		return new ResourceLocation(Techarium.MOD_ID, "textures/block/animated/botarium.png");
	}

	@Override
	public ResourceLocation getAnimationResource(BotariumBlockEntity animatable) {
		return new ResourceLocation(Techarium.MOD_ID, "animations/botarium.animation.json");
	}

}
