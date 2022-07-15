package com.techarium.techarium.client.util.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.Nullable;

public class FluidClientUtilsImpl {
	public static ResourceLocation getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
		return view == null || pos == null ? extensions.getStillTexture() : extensions.getStillTexture(view.getFluidState(pos), view, pos);
	}

	public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
		IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
		return view == null || pos == null ? extensions.getTintColor() : extensions.getTintColor(view.getFluidState(pos), view, pos);
	}
}
