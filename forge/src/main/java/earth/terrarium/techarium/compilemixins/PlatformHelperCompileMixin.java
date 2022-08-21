package earth.terrarium.techarium.compilemixins;

import earth.terrarium.techarium.inventory.ExtraDataMenuProvider;
import earth.terrarium.techarium.util.PlatformHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = PlatformHelper.class, remap = false)
public class PlatformHelperCompileMixin {
	@Overwrite
	public static boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Overwrite
	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		NetworkHooks.openScreen(player, provider, (data) -> provider.writeExtraData(player, data));
	}

	@Overwrite
	public static Fluid determineFluidFromItem(ItemStack stack) {
		FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
		if (fluidStack.isEmpty()) {
			return Fluids.EMPTY;
		}
		return fluidStack.getFluid();
	}

	@Overwrite
	public static Component getFluidName(Fluid fluid) {
		return fluid.getFluidType().getDescription();
	}
}
