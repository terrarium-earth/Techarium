package earth.terrarium.techarium.client.screen;

import earth.terrarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;

public class TechariumMenuScreens {

	public static void register() {
		MenuScreens.register(TechariumMenuTypes.BOTARIUM.get(), BotariumScreen::new);
		MenuScreens.register(TechariumMenuTypes.EXCHANGE_STATION.get(), ExchangeStationScreen::new);
		MenuScreens.register(TechariumMenuTypes.MACHINE_CORE.get(), MachineCoreScreen::new);
	}
}
