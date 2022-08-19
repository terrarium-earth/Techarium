package earth.terrarium.techarium.registry;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.inventory.BotariumMenu;
import earth.terrarium.techarium.inventory.ExchangeStationMenu;
import earth.terrarium.techarium.inventory.MachineCoreMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class TechariumMenuTypes {
	public static final TechariumRegistrar<MenuType<?>> REGISTRAR = new TechariumRegistrar<>(Techarium.MOD_ID, Registry.MENU);

	public static final Supplier<MenuType<BotariumMenu>> BOTARIUM = REGISTRAR.register("botarium", () -> RegistryHelper.createMenuType((id, inv, pos) -> new BotariumMenu(id, inv, inv.player, pos)));
	public static final Supplier<MenuType<ExchangeStationMenu>> EXCHANGE_STATION = REGISTRAR.register("exchange_station", () -> RegistryHelper.createMenuType((id, inv, pos) -> new ExchangeStationMenu(id, inv, inv.player, pos)));
	public static final Supplier<MenuType<MachineCoreMenu>> MACHINE_CORE = REGISTRAR.register("machine_core", () -> RegistryHelper.createMenuType((id, inv, pos) -> new MachineCoreMenu(id, inv, inv.player, pos)));

}
