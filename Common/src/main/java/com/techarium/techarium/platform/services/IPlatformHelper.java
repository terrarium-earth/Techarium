package com.techarium.techarium.platform.services;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Consumer;

public interface IPlatformHelper {

	/**
	 * Gets the name of the current platform
	 *
	 * @return The name of the current platform.
	 */
	String getPlatformName();

	/**
	 * Checks if a mod with the given id is loaded.
	 *
	 * @param modId The mod to check if it is loaded.
	 * @return True if the mod is loaded, false otherwise.
	 */
	boolean isModLoaded(String modId);

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
	 * @param extraData extra data to send to the client.
	 */
	void openGui(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData);

	Fluid determineFluidFromItem(ItemStack stack);

	/**
	 * @return the volume of a bucket. Yes it is not the same between Forge and Fabric.
	 */
	long getBucketVolume();

}
