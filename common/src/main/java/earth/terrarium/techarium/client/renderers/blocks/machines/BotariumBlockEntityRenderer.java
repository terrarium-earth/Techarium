package earth.terrarium.techarium.client.renderers.blocks.machines;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.techarium.client.renderers.blocks.base.CustomGeoBlockRenderer;
import earth.terrarium.techarium.common.blockentities.machines.BotariumBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class BotariumBlockEntityRenderer extends CustomGeoBlockRenderer<BotariumBlockEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public BotariumBlockEntityRenderer(RegistryEntry<Block> block) {
        super(block);
        blockRenderer = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void postRender(PoseStack poseStack, BotariumBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        // render water drop particles
        ClientLevel level = Minecraft.getInstance().level;
        if (!Minecraft.getInstance().isPaused() && level.getGameTime() % 10 == 0 && animatable.cookTimeTotal() > 0) {
            level.addParticle(ParticleTypes.FALLING_WATER, animatable.getBlockPos().getX() + 0.5, animatable.getBlockPos().getY() + 1.5, animatable.getBlockPos().getZ() + 0.5, 0, 0, 0);
        }

        try (var ignored = new CloseablePoseStack(poseStack)) {
            ItemStack stack = animatable.getItem(5);
            if (stack.isEmpty()) return;
            Block fertilizerBlock = Block.byItem(stack.getItem());
            if (fertilizerBlock == Blocks.AIR) return;
            if (fertilizerBlock == Blocks.DIRT) fertilizerBlock = Blocks.FARMLAND;
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(-0.5, 0.5, -0.5);
            BlockState fertilizerState = fertilizerBlock.defaultBlockState();
            if (fertilizerBlock instanceof FarmBlock) {
                fertilizerState = fertilizerState.setValue(FarmBlock.MOISTURE, 7);
            }
            blockRenderer.renderSingleBlock(fertilizerState, poseStack, bufferSource, packedLight, packedOverlay);
        }

        try (var ignored = new CloseablePoseStack(poseStack)) {
            Block cropBlock = animatable.cropBlock();
            if (cropBlock == null) return;
            BlockState cropState = cropBlock.defaultBlockState();
            if (cropBlock instanceof CropBlock crop) {
                int age = (int) Math.ceil(animatable.cookTime() / (float) Math.max(1, animatable.cookTimeTotal()) * crop.getMaxAge());
                cropState = crop.getStateForAge(age);
            } else if (cropBlock instanceof StemBlock) {
                int age = (int) Math.ceil(animatable.cookTime() / (float) Math.max(1, animatable.cookTimeTotal()) * StemBlock.MAX_AGE);
                cropState = cropState.setValue(StemBlock.AGE, age);
            } else if (cropBlock instanceof CocoaBlock) {
                int age = (int) Math.ceil(animatable.cookTime() / (float) Math.max(1, animatable.cookTimeTotal()) * CocoaBlock.MAX_AGE);
                cropState = cropState.setValue(CocoaBlock.AGE, age);
                poseStack.translate(0, 1, 1);
                poseStack.mulPose(Axis.XN.rotationDegrees(90));
            }

            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.translate(-0.5, 1.5, -0.5);
            blockRenderer.renderSingleBlock(cropState, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }
}
