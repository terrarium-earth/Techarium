package earth.terrarium.techarium.fabric.client.extensions;

import earth.terrarium.techarium.client.util.FluidClientUtils;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidClientUtils.class)
public class FluidClientUtilsImpl {
	@ImplementsBaseElement
	public static TextureAtlasSprite getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(view, pos, fluid.defaultFluidState())[0];
	}

	@ImplementsBaseElement
	public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(view, pos, fluid.defaultFluidState());
	}
}
