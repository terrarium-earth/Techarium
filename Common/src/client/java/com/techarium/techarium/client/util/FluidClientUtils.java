package com.techarium.techarium.client.util;

import com.techarium.techarium.util.extensions.ExtendableDeclaration;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

@ExtendableDeclaration
public class FluidClientUtils {
	@ExtendableDeclaration
	public static TextureAtlasSprite getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new UnsupportedOperationException("getStillTexture was not implemented.");
	}

	@ExtendableDeclaration
	public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new UnsupportedOperationException("getFluidColor was not implemented.");
	}
}
