package com.techarium.techarium.fabric;

import com.techarium.techarium.fabric.client.render.BotariumRenderer;
import com.techarium.techarium.fabric.client.render.ExchangeStationRenderer;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumMenuScreens;
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
