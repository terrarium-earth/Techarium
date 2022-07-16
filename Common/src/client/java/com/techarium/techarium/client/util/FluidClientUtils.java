package com.techarium.techarium.client.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class FluidClientUtils {
	@ExpectPlatform
	public static ResourceLocation getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new UnsupportedOperationException("getStillTexture was not implemented.");
	}

	@ExpectPlatform
	public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		throw new UnsupportedOperationException("getFluidColor was not implemented.");
	}
}
