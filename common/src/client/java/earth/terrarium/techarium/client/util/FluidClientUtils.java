package earth.terrarium.techarium.client.util;

import earth.terrarium.techarium.util.ImplementedByMixin;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class FluidClientUtils {
	@ImplementedByMixin
	public static TextureAtlasSprite getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new NotImplementedException("getStillTexture was not implemented.");
	}

	@ImplementedByMixin
	public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new NotImplementedException("getFluidColor was not implemented.");
	}
}
