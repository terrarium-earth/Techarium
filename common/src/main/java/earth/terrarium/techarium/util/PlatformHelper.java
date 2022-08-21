package earth.terrarium.techarium.util;

import earth.terrarium.techarium.inventory.ExtraDataMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;

public class PlatformHelper {
	/**
	 * Check if the game is currently in a development environment.
	 *
	 * @return True if in a development environment, false otherwise.
	 */
	@ImplementedByMixin
	public static boolean isDevelopmentEnvironment() {
		throw new NotImplementedException("isDevelopmentEnvironment was not implemented.");
	}

	/**
	 * Open a gui.
	 *
	 * @param player   the player.
	 * @param provider the menu provider.
	 */
	@ImplementedByMixin
	public static void openMenu(ServerPlayer player, ExtraDataMenuProvider provider) {
		throw new NotImplementedException("openMenu was not implemented.");
	}

	@ImplementedByMixin
	public static Fluid determineFluidFromItem(ItemStack stack) {
		throw new NotImplementedException("determineFluidFromItem was not implemented.");
	}

	@ImplementedByMixin
	public static Component getFluidName(Fluid fluid) {
		throw new NotImplementedException("getFluidName was not implemented.");
	}
}
