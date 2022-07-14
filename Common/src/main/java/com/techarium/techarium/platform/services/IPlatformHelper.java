package com.techarium.techarium.platform.services;

import com.techarium.techarium.inventory.ExtraDataMenuProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public interface IPlatformHelper {
	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	boolean isDevelopmentEnvironment();

	/**
	 * Open a gui.
	 *
	 * @param player    the player.
	 * @param provider  the menu provider.
	 */
	void openMenu(ServerPlayer player, ExtraDataMenuProvider provider);

	FluidHelper getFluidHelper();

	/**
	 * A simple interface to regroup fluid helper methods
	 */
	interface FluidHelper {

		Fluid determineFluidFromItem(ItemStack stack);

		TextureAtlasSprite getStillTexture(Fluid fluid);

		int getFluidColor(Fluid fluid);

		Component getFluidName(Fluid fluid);
	}
}
