package com.techarium.techarium.client.util.fabric;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

public class FluidClientUtilsImpl {
	public static ResourceLocation getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(view, pos, fluid.defaultFluidState())[0].getName();
	}

	public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(view, pos, fluid.defaultFluidState());
	}
}
