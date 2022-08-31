package earth.terrarium.techarium.fabric.client;

import earth.terrarium.techarium.fabric.client.render.BotariumRenderer;
import earth.terrarium.techarium.fabric.client.render.ExchangeStationRenderer;
import earth.terrarium.techarium.client.screen.TechariumMenuScreens;
import earth.terrarium.techarium.fabric.client.render.GravMagnetRenderer;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class TechariumFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.register(TechariumBlockEntities.EXCHANGE_STATION.get(), (renderer) -> new ExchangeStationRenderer());
		BlockEntityRendererRegistry.register(TechariumBlockEntities.BOTARIUM.get(), (renderer) -> new BotariumRenderer());
		BlockEntityRendererRegistry.register(TechariumBlockEntities.GRAVMAGNET.get(), (renderer) -> new GravMagnetRenderer());

		TechariumMenuScreens.register();
	}

}
