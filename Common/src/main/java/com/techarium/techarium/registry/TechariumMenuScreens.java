package com.techarium.techarium.registry;

import com.techarium.techarium.client.screen.BotariumScreen;
import com.techarium.techarium.client.screen.ExchangeStationScreen;
import com.techarium.techarium.client.screen.MachineCoreScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class TechariumMenuScreens {

	public static void register() {
		MenuScreens.register(TechariumMenuTypes.BOTARIUM.get(), BotariumScreen::new);
		MenuScreens.register(TechariumMenuTypes.EXCHANGE_STATION.get(), ExchangeStationScreen::new);
		MenuScreens.register(TechariumMenuTypes.MACHINE_CORE.get(), MachineCoreScreen::new);
	}

}
