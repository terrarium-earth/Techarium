package com.techarium.techarium.client.events;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.client.render.BotariumRenderer;
import com.techarium.techarium.client.render.ExchangeStationRenderer;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.client.screen.TechariumMenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Techarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventHandler {

	@SubscribeEvent
	public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		BlockEntityRenderers.register(TechariumBlockEntities.EXCHANGE_STATION.get(), ExchangeStationRenderer::new);
		BlockEntityRenderers.register(TechariumBlockEntities.BOTARIUM.get(), BotariumRenderer::new);
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		TechariumMenuScreens.register();
	}
}
