package com.techarium.techarium.util;

import com.techarium.techarium.inventory.ExtraDataMenuProvider;
import com.techarium.techarium.util.extensions.ExtendableDeclaration;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;

@ExtendableDeclaration
public class PlatformHelper {
	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	@ExtendableDeclaration
	public static boolean isDevelopmentEnvironment() {
		throw new NotImplementedException("isDevelopmentEnvironment was not implemented.");
	}

	/**
	 * Open a gui.
	 *
	 * @param player   the player.
	 * @param provider the menu provider.
	 */
	@ExtendableDeclaration
	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		throw new NotImplementedException("openMenu was not implemented.");
	}

	@ExtendableDeclaration
	public static Fluid determineFluidFromItem(ItemStack stack) {
		throw new NotImplementedException("determineFluidFromItem was not implemented.");
	}

	@ExtendableDeclaration
	public static Component getFluidName(Fluid fluid) {
		throw new NotImplementedException("getFluidName was not implemented.");
	}
}
