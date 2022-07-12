package com.techarium.techarium.platform;

import com.techarium.techarium.platform.services.IPlatformHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.RenderProperties;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Consumer;

public class ForgePlatformHelper implements IPlatformHelper {

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
		NetworkHooks.openGui(player, provider, extraData);
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
		public int getBucketVolume() {
			return FluidType.BUCKET_VOLUME;
		}

		@Override
		public TextureAtlasSprite getStillTexture(Fluid fluid) {
			ResourceLocation texture = RenderProperties.get(fluid).getStillTexture();
			return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
		}

		@Override
		public int getFluidColor(Fluid fluid) {
			return RenderProperties.get(fluid).getColorTint();
		}

		@Override
		public int toKekieBucket(int amount) {
			return amount;
		}

		@Override
		public Component getFluidName(Fluid fluid) {
			return fluid.getFluidType().getDescription();
		}

	}

}
