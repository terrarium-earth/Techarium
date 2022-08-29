package earth.terrarium.techarium.forge.extensions;

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
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

@ClassExtension(PlatformHelper.class)
public class PlatformHelperImpl {
    @ImplementsBaseElement
    public static boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @ImplementsBaseElement
    public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
        NetworkHooks.openScreen(player, provider, (data) -> provider.writeExtraData(player, data));
    }

    @ImplementsBaseElement
    public static Fluid determineFluidFromItem(ItemStack stack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(stack).orElse(FluidStack.EMPTY);
        if (fluidStack.isEmpty()) {
            return Fluids.EMPTY;
        }
        return fluidStack.getFluid();
    }

    @ImplementsBaseElement
    public static Component getFluidName(Fluid fluid) {
        return fluid.getFluidType().getDescription();
    }
}
