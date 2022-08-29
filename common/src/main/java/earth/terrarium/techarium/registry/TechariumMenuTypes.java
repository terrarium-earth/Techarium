package earth.terrarium.techarium.registry;

import earth.terrarium.botarium.api.RegistryHelpers;
import earth.terrarium.botarium.api.RegistryHolder;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.inventory.BotariumMenu;
import earth.terrarium.techarium.inventory.ExchangeStationMenu;
import earth.terrarium.techarium.inventory.MachineCoreMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class TechariumMenuTypes {
	public static final RegistryHolder<MenuType<?>> MENUS = new RegistryHolder<>(Registry.MENU, Techarium.MOD_ID);

	public static final Supplier<MenuType<BotariumMenu>> BOTARIUM = MENUS.register("botarium", () -> RegistryHelper.createMenuType(BotariumMenu::new));
	public static final Supplier<MenuType<ExchangeStationMenu>> EXCHANGE_STATION = MENUS.register("exchange_station", () -> RegistryHelper.createMenuType(ExchangeStationMenu::new));
	public static final Supplier<MenuType<MachineCoreMenu>> MACHINE_CORE = MENUS.register("machine_core", () -> RegistryHelper.createMenuType(MachineCoreMenu::new));

}
