package earth.terrarium.techarium.compilemixins;

import earth.terrarium.techarium.fabric.inventory.ExtraDataMenuProviderWrapper;
import earth.terrarium.techarium.inventory.ExtraDataMenuProvider;
import earth.terrarium.techarium.util.PlatformHelper;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PlatformHelper.class, remap = false)
public class PlatformHelperCompileMixin {
	@Overwrite
	public static boolean isDevelopmentEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Overwrite
	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		player.openMenu(new ExtraDataMenuProviderWrapper(provider));
	}

	@Overwrite
	public static Fluid determineFluidFromItem(ItemStack stack) {
		Storage<FluidVariant> storage = ContainerItemContext.withInitial(stack).find(FluidStorage.ITEM);
		if (storage == null) {
			return Fluids.EMPTY;
		}
		for (StorageView<FluidVariant> storageView : storage) {
			return storageView.getResource().getFluid();
		}
		return Fluids.EMPTY;
	}

	@Overwrite
	public static Component getFluidName(Fluid fluid) {
		return FluidVariantAttributes.getName(FluidVariant.of(fluid));
	}
}
