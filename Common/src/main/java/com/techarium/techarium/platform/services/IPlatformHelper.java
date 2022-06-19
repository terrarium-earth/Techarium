package com.techarium.techarium.platform.services;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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

	// TODO @Ketheroth: 18/06/2022 move fluid methods in subclass

	Fluid determineFluidFromItem(ItemStack stack);

	/**
	 * @return the volume of a bucket. Yes it is not the same between Forge and Fabric.
	 */
	long getBucketVolume();

	TextureAtlasSprite getStillTexture(Fluid fluid);

	int getFluidColor(Fluid fluid);

	/**
	 * Convert an amount in the smallest unit (mB or droplet) of a fluid to kekie-buckets (millibucket in disguise)
	 *
	 * @param amount the amount to convert
	 * @return the amount converted in kekie-bucket
	 */
	long toKekieBucket(long amount);

	Component getFluidName(Fluid fluid);

}
