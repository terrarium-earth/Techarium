package earth.terrarium.techarium.client;

import earth.terrarium.botarium.client.ClientHooks;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.client.config.TechariumConfigClient;
import earth.terrarium.techarium.client.renderers.blocks.base.CustomGeoBlockRenderer;
import earth.terrarium.techarium.common.registry.ModBlockEntityTypes;
import earth.terrarium.techarium.common.registry.ModBlocks;

public class TechariumClient {

    public static void init() {
        Techarium.CONFIGURATOR.registerConfig(TechariumConfigClient.class);
        registerBlockEntityRenderers();
    }

    private static void registerBlockEntityRenderers() {
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.BOTARIUM.get(), context -> new CustomGeoBlockRenderer<>(ModBlocks.BOTARIUM));
    }
}
