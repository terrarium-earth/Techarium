package com.techarium.techarium.client;

import com.techarium.techarium.client.render.BotariumRenderer;
import com.techarium.techarium.client.render.ExchangeStationRenderer;
import com.techarium.techarium.client.screen.TechariumMenuScreens;
import com.techarium.techarium.registry.TechariumBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class TechariumFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(TechariumBlockEntities.EXCHANGE_STATION.get(), (renderer) -> new ExchangeStationRenderer());
		BlockEntityRendererRegistry.register(TechariumBlockEntities.BOTARIUM.get(), (renderer) -> new BotariumRenderer());

		TechariumMenuScreens.register();
	}

}
