package com.techarium.techarium.platform;

import com.techarium.techarium.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public String getPlatformName() {
		return "Fabric";
	}

	@Override
	public boolean isModLoaded(String modId) {

		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevelopmentEnvironment() {

		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public void openGui(ServerPlayer player, MenuProvider provider, Consumer<FriendlyByteBuf> extraData) {
		ExtendedScreenHandlerFactory factory = new ExtendedScreenHandlerFactory() {
			private final MenuProvider prov = provider;
			@Nullable
			@Override
			public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
				return prov.createMenu(i, inventory, player);
			}

			@Override
			public Component getDisplayName() {
				return prov.getDisplayName();
			}

			@Override
			public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
				extraData.accept(buf);
			}
		};
		player.openMenu(factory);
	}

	@Override
	public Fluid determineFluidFromItem(ItemStack stack) {
		Storage<FluidVariant> storage = ContainerItemContext.withInitial(stack).find(FluidStorage.ITEM);
		if (storage == null) {
			return Fluids.EMPTY;
		}
		for (StorageView<FluidVariant> storageView : storage) {
			return storageView.getResource().getFluid();
		}
		return Fluids.EMPTY;
	}

	@Override
	public long getBucketVolume() {
		return FluidConstants.BUCKET;
	}

}
