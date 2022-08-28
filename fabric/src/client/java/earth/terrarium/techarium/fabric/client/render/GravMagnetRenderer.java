package earth.terrarium.techarium.fabric.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.techarium.block.entity.selfdeploying.BotariumBlockEntity;
import earth.terrarium.techarium.block.entity.singleblock.GravMagnetBlockEntity;
import earth.terrarium.techarium.client.model.BotariumModel;
import earth.terrarium.techarium.client.model.GravMagnetModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;


public class GravMagnetRenderer extends GeoBlockRenderer<GravMagnetBlockEntity> {

	public GravMagnetRenderer() {
		super(new GravMagnetModel());
	}

	@Override
	public RenderType getRenderType(GravMagnetBlockEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
	}

	@Override
	public void renderEarly(GravMagnetBlockEntity animatable, PoseStack stackIn, float ticks,
							MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn,
							float red, float green, float blue, float partialTicks) {

		BlockState state = animatable.getLevel().getBlockState(animatable.getBlockPos());
		DirectionProperty directionState = BlockStateProperties.FACING;
		if (state.hasProperty(directionState)) {
			Direction dir = state.getValue(directionState);
			stackIn.translate(0, 0, 0);
			if (dir == Direction.UP)
				stackIn.translate(0, -0.5, -0.5);
			else if (dir == Direction.DOWN)
				stackIn.translate(0, -0.5, 0.5);
		}

		super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red,
				green, blue, partialTicks);
	}

}
