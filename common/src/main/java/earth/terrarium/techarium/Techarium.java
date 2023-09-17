package earth.terrarium.techarium;

import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import earth.terrarium.techarium.common.networking.NetworkHandler;
import earth.terrarium.techarium.common.registry.ModBlockEntityTypes;
import earth.terrarium.techarium.common.registry.ModBlocks;
import earth.terrarium.techarium.common.registry.ModItems;
import earth.terrarium.techarium.common.registry.ModSoundEvents;
import earth.terrarium.techarium.common.config.TechariumConfig;

public class Techarium {

    public static final String MOD_ID = "techarium";
    public static final Configurator CONFIGURATOR = new Configurator();

    public static void init() {
        CONFIGURATOR.registerConfig(TechariumConfig.class);
        NetworkHandler.init();

        ModBlocks.BLOCKS.init();
        ModItems.ITEMS.init();
        ModBlockEntityTypes.BLOCK_ENTITY_TYPES.init();
        ModSoundEvents.SOUND_EVENTS.init();
    }
}
