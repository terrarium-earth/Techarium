package earth.terrarium.techarium.client.model;

import earth.terrarium.techarium.block.entity.selfdeploying.BotariumBlockEntity;
import earth.terrarium.techarium.util.Utils;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BotariumModel extends AnimatedGeoModel<BotariumBlockEntity> {

	public static final ResourceLocation MODEL = Utils.resourceLocation("geo/botarium.geo.json");
	public static final ResourceLocation TEXTURE = Utils.resourceLocation("textures/block/animated/botarium.png");
	public static final ResourceLocation ANIMATION = Utils.resourceLocation("animations/botarium.animation.json");

	@Override
	public ResourceLocation getModelResource(BotariumBlockEntity object) {
		return MODEL;
	}

	@Override
	public ResourceLocation getTextureResource(BotariumBlockEntity object) {
		return TEXTURE;
	}

	@Override
	public ResourceLocation getAnimationResource(BotariumBlockEntity animatable) {
		return ANIMATION;
	}

}
