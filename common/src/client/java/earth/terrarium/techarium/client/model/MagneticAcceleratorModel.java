package earth.terrarium.techarium.client.model;

import earth.terrarium.techarium.block.entity.singleblock.MagneticAcceleratorBlockEntity;
import earth.terrarium.techarium.util.Utils;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MagneticAcceleratorModel extends AnimatedGeoModel<MagneticAcceleratorBlockEntity> {
    public static final ResourceLocation MODEL = Utils.resourceLocation("geo/magnetic_accelerator.geo.json");
    public static final ResourceLocation PULL_TEXTURE = Utils.resourceLocation("textures/block/animated/magnetic_accelerator_pull.png");
    public static final ResourceLocation PUSH_TEXTURE = Utils.resourceLocation("textures/block/animated/magnetic_accelerator_push.png");
    public static final ResourceLocation ANIMATION = Utils.resourceLocation("animations/magnetic_accelerator.animation.json");

    @Override
    public ResourceLocation getModelResource(MagneticAcceleratorBlockEntity tile) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(MagneticAcceleratorBlockEntity tile) {
        return tile.isPull() ? PULL_TEXTURE : PUSH_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(MagneticAcceleratorBlockEntity tile) {
        return ANIMATION;
    }
}
