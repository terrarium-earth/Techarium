package com.techarium.techarium.forge.platform;

import com.techarium.techarium.inventory.ExtraDataMenuProvider;
import com.techarium.techarium.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkHooks;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		NetworkHooks.openGui(player, provider, (data) -> provider.writeExtraData(player, data));
	}

	@Override
	public FluidHelper getFluidHelper() {
		return ForgeFluidHelper.INSTANCE;
	}

	public static class ForgeFluidHelper implements FluidHelper {

		public static final ForgeFluidHelper INSTANCE = new ForgeFluidHelper();

		private ForgeFluidHelper() {
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
		public TextureAtlasSprite getStillTexture(Fluid fluid) {
			ResourceLocation texture = IClientFluidTypeExtensions.of(fluid).getStillTexture();
			return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
		}

		@Override
		public int getFluidColor(Fluid fluid) {
			return IClientFluidTypeExtensions.of(fluid).getTintColor();
		}

		@Override
		public Component getFluidName(Fluid fluid) {
			return fluid.getFluidType().getDescription();
		}
	}
}
