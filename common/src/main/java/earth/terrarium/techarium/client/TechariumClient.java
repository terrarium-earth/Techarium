package earth.terrarium.techarium.client;

import earth.terrarium.botarium.client.ClientHooks;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.client.config.TechariumConfigClient;
import earth.terrarium.techarium.client.renderers.blocks.machines.BotariumBlockEntityRenderer;
import earth.terrarium.techarium.client.renderers.items.base.CustomGeoItemRenderer;
import earth.terrarium.techarium.client.screens.BotariumScreen;
import earth.terrarium.techarium.common.registry.ModBlockEntityTypes;
import earth.terrarium.techarium.common.registry.ModBlocks;
import earth.terrarium.techarium.common.registry.ModMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;

public class TechariumClient {
    private static final Map<Item, BlockEntityWithoutLevelRenderer> ITEM_RENDERERS = new HashMap<>();

    public static void init() {
        Techarium.CONFIGURATOR.registerConfig(TechariumConfigClient.class);
        registerScreens();
        registerBlockRenderTypes();
        registerBlockEntityRenderers();
        registerItemRenderers();
    }

    private static void registerScreens() {
        MenuScreens.register(ModMenus.BOTARIUM.get(), BotariumScreen::new);
    }

    private static void registerBlockRenderTypes() {
        ClientHooks.setRenderLayer(ModBlocks.ALUMINIUM_LADDER.get(), RenderType.cutout());
        ClientHooks.setRenderLayer(ModBlocks.METAL_SCAFFOLDING.get(), RenderType.cutout());
        ClientHooks.setRenderLayer(ModBlocks.CORN.get(), RenderType.cutout());
    }

    private static void registerBlockEntityRenderers() {
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.BOTARIUM.get(), context -> new BotariumBlockEntityRenderer(ModBlocks.BOTARIUM));
    }

    private static void registerItemRenderers() {
        ITEM_RENDERERS.put(ModBlocks.BOTARIUM.get().asItem(), new CustomGeoItemRenderer(ModBlocks.BOTARIUM));
    }

    public static BlockEntityWithoutLevelRenderer getItemRenderer(ItemLike item) {
        return ITEM_RENDERERS.get(item.asItem());
    }
}
