package earth.terrarium.techarium.client.model;

import earth.terrarium.techarium.block.entity.singleblock.GravMagnetBlockEntity;
import earth.terrarium.techarium.util.Utils;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GravMagnetModel extends AnimatedGeoModel<GravMagnetBlockEntity> {
    public static final ResourceLocation MODEL = Utils.resourceLocation("geo/gravmagnet.geo.json");
    public static final ResourceLocation PULL_TEXTURE = Utils.resourceLocation("textures/block/animated/gravmagnet.png");
    public static final ResourceLocation PUSH_TEXTURE = Utils.resourceLocation("textures/block/animated/gravmagnet.png");
    public static final ResourceLocation ANIMATION = Utils.resourceLocation("animations/gravmagnet.animation.json");

    @Override
    public ResourceLocation getModelResource(GravMagnetBlockEntity tile) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GravMagnetBlockEntity tile) {
        return tile.isPull() ? PULL_TEXTURE : PUSH_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(GravMagnetBlockEntity tile) {
        return ANIMATION;
    }
}
