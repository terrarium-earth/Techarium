package com.techarium.techarium.client.events;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.client.render.BotariumRenderer;
import com.techarium.techarium.client.render.ExchangeStationRenderer;
import com.techarium.techarium.client.screen.BotariumScreen;
import com.techarium.techarium.client.screen.ExchangeStationScreen;
import com.techarium.techarium.client.screen.MachineCoreScreen;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Techarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(TechariumBlockEntities.EXCHANGE_STATION.get(), ExchangeStationRenderer::new);
		event.registerBlockEntityRenderer(TechariumBlockEntities.BOTARIUM.get(), BotariumRenderer::new);
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		MenuScreens.register(TechariumMenuTypes.BOTARIUM.get(), BotariumScreen::new);
		MenuScreens.register(TechariumMenuTypes.EXCHANGE_STATION.get(), ExchangeStationScreen::new);
		MenuScreens.register(TechariumMenuTypes.MACHINE_CORE.get(), MachineCoreScreen::new);
	}

}
