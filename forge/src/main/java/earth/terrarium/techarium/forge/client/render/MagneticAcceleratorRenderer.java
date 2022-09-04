package earth.terrarium.techarium.forge.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.techarium.block.entity.singleblock.MagneticAcceleratorBlockEntity;
import earth.terrarium.techarium.client.model.MagneticAcceleratorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class MagneticAcceleratorRenderer extends GeoBlockRenderer<MagneticAcceleratorBlockEntity> {

	public MagneticAcceleratorRenderer(BlockEntityRendererProvider.Context context) {
		super(context, new MagneticAcceleratorModel());
	}

	@Override
	public RenderType getRenderType(MagneticAcceleratorBlockEntity animatable, float partialTicks, PoseStack stack, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
		return RenderType.entityCutoutNoCull(this.getTextureLocation(animatable));
	}

	@Override
	public void renderEarly(MagneticAcceleratorBlockEntity animatable, PoseStack stackIn, float ticks,
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
