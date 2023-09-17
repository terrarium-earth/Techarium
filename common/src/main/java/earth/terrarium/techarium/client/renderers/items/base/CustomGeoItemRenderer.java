package earth.terrarium.techarium.client.renderers.items.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.items.base.CustomGeoBlockItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CustomGeoItemRenderer extends GeoItemRenderer<CustomGeoBlockItem> {
    private final ResourceLocation texture;

    public CustomGeoItemRenderer(RegistryEntry<Block> block) {
        this(block, new DefaultedBlockGeoModel<>(block.getId()));
    }

    public CustomGeoItemRenderer(RegistryEntry<Block> block, GeoModel<CustomGeoBlockItem> model) {
        super(model);
        this.texture = new ResourceLocation(Techarium.MOD_ID, "textures/block/%s.png".formatted(block.getId().getPath()));
    }

    @Override
    public void actuallyRender(PoseStack poseStack, CustomGeoBlockItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        try (var pose = new CloseablePoseStack(poseStack)) {
            pose.translate(0.0f, -0.5, 0.0f);
            super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CustomGeoBlockItem animatable) {
        return this.texture;
    }
}
