package earth.terrarium.techarium.forge.client.events;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.client.screen.TechariumMenuScreens;
import earth.terrarium.techarium.forge.client.render.BotariumRenderer;
import earth.terrarium.techarium.forge.client.render.ExchangeStationRenderer;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
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
