package com.techarium.techarium.client;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.client.render.ExchangeStationRenderer;
import com.techarium.techarium.registry.TechariumBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Techarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(TechariumBlockEntities.EXCHANGE_STATION.get(), ExchangeStationRenderer::new);
	}

}
