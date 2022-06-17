package com.techarium.techarium.platform;

import com.techarium.techarium.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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

}
