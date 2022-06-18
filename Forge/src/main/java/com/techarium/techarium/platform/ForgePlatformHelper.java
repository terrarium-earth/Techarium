package com.techarium.techarium.platform;

import com.techarium.techarium.platform.services.IPlatformHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Consumer;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public String getPlatformName() {

		return "Forge";
	}

	@Override
	public boolean isModLoaded(String modId) {

		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {

		return !FMLLoader.isProduction();
	}

	@Override
	public void openGui(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData) {
		NetworkHooks.openGui( player, provider, extraData);
	}

	@Override
	public Fluid determineFluidFromItem(ItemStack stack) {
		FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
		if (fluidStack.isEmpty()) {
			return Fluids.EMPTY;
		}
		return fluidStack.getFluid();
	}

	@Override
	public long getBucketVolume() {
		return FluidAttributes.BUCKET_VOLUME;
	}

}
