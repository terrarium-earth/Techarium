package com.techarium.techarium.client.util;

import com.techarium.techarium.util.extensions.ExtendableDeclaration;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

@ExtendableDeclaration
public class FluidClientUtils {
	@ExtendableDeclaration
	public static TextureAtlasSprite getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new NotImplementedException("getStillTexture was not implemented.");
	}

	@ExtendableDeclaration
	public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new NotImplementedException("getFluidColor was not implemented.");
	}
}
