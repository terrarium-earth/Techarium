package com.techarium.techarium;

import com.techarium.techarium.client.render.BotariumRenderer;
import com.techarium.techarium.client.render.ExchangeStationRenderer;
import com.techarium.techarium.client.screen.BotariumScreen;
import com.techarium.techarium.client.screen.ExchangeStationScreen;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class TechariumFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(TechariumBlockEntities.EXCHANGE_STATION.get(), (renderer) -> new ExchangeStationRenderer());
		BlockEntityRendererRegistry.register(TechariumBlockEntities.BOTARIUM.get(), (renderer) -> new BotariumRenderer());

		MenuScreens.register(TechariumMenuTypes.BOTARIUM.get(), BotariumScreen::new);
		MenuScreens.register(TechariumMenuTypes.EXCHANGE_STATION.get(), ExchangeStationScreen::new);
	}

}
