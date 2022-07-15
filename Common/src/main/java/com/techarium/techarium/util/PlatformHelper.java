package com.techarium.techarium.util;

import com.techarium.techarium.inventory.ExtraDataMenuProvider;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class PlatformHelper {
	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	@ExpectPlatform
	public static boolean isDevelopmentEnvironment() {
		throw new UnsupportedOperationException("isDevelopmentEnvironment was not implemented.");
	}

	/**
	 * Open a gui.
	 *
	 * @param player   the player.
	 * @param provider the menu provider.
	 */
	@ExpectPlatform
	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		throw new UnsupportedOperationException("openMenu was not implemented.");
	}

	@ExpectPlatform
	public static Fluid determineFluidFromItem(ItemStack stack) {
		throw new UnsupportedOperationException("determineFluidFromItem was not implemented.");
	}

	@ExpectPlatform
	public static Component getFluidName(Fluid fluid) {
		throw new UnsupportedOperationException("getFluidName was not implemented.");
	}
}
