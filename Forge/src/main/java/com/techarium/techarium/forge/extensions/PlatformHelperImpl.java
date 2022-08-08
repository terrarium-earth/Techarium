package com.techarium.techarium.forge.extensions;

import com.techarium.techarium.inventory.ExtraDataMenuProvider;
import com.techarium.techarium.util.PlatformHelper;
import com.techarium.techarium.util.extensions.ExtensionFor;
import com.techarium.techarium.util.extensions.ExtensionImplementation;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkHooks;

@ExtensionFor(PlatformHelper.class)
public class PlatformHelperImpl {
	@ExtensionImplementation
	public static boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@ExtensionImplementation
	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		NetworkHooks.openGui(player, provider, (data) -> provider.writeExtraData(player, data));
	}

	@ExtensionImplementation
	public static Fluid determineFluidFromItem(ItemStack stack) {
		FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
		if (fluidStack.isEmpty()) {
			return Fluids.EMPTY;
		}
		return fluidStack.getFluid();
	}

	@ExtensionImplementation
	public static Component getFluidName(Fluid fluid) {
		return fluid.getFluidType().getDescription();
	}
}
