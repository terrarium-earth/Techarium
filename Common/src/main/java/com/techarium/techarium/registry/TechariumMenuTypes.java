package com.techarium.techarium.registry;

import com.techarium.techarium.inventory.BotariumMenu;
import com.techarium.techarium.inventory.ExchangeStationMenu;
import com.techarium.techarium.inventory.MachineCoreMenu;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class TechariumMenuTypes {

	public static final Supplier<MenuType<BotariumMenu>> BOTARIUM = CommonServices.REGISTRY.registerMenuType("botarium", (id, inv, pos) -> new BotariumMenu(id, inv, inv.player, pos));
	public static final Supplier<MenuType<ExchangeStationMenu>> EXCHANGE_STATION = CommonServices.REGISTRY.registerMenuType("exchange_station", (id, inv, pos) -> new ExchangeStationMenu(id, inv, inv.player, pos));
	public static final Supplier<MenuType<MachineCoreMenu>> MACHINE_CORE = CommonServices.REGISTRY.registerMenuType("machine_core", (id, inv, pos) -> new MachineCoreMenu(id, inv, inv.player, pos));

	public static void register() {}

}
