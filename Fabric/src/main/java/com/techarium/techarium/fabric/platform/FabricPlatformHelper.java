package com.techarium.techarium.fabric.platform;

import com.techarium.techarium.fabric.inventory.ExtraDataMenuProviderWrapper;
import com.techarium.techarium.inventory.ExtraDataMenuProvider;
import com.techarium.techarium.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		player.openMenu(new ExtraDataMenuProviderWrapper(provider));
	}

	@Override
	public FluidHelper getFluidHelper() {
		return FabricFluidHelper.INSTANCE;
	}

	public static class FabricFluidHelper implements FluidHelper {

		public static final FabricFluidHelper INSTANCE = new FabricFluidHelper();

		private FabricFluidHelper() {
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
		public TextureAtlasSprite getStillTexture(Fluid fluid) {
			ResourceLocation texture = FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidSprites(null, null, fluid.defaultFluidState())[0].getName();
			return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
		}

		@Override
		public int getFluidColor(Fluid fluid) {
			return FluidRenderHandlerRegistry.INSTANCE.get(fluid).getFluidColor(null, null, fluid.defaultFluidState());
		}

		@Override
		public Component getFluidName(Fluid fluid) {
			return FluidVariantAttributes.getName(FluidVariant.of(fluid));
		}

	}
}
