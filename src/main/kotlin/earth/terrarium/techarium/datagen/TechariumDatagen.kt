package earth.terrarium.techarium.datagen

import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.datagen.providers.*
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.data.event.GatherDataEvent

@Mod(TechariumConstants.MOD_ID)
class TechariumDatagen(modBus: IEventBus, mod: ModContainer) {
    init {
        modBus.addListener(::gatherDataEvent)
    }

    private fun gatherDataEvent(event: GatherDataEvent) {
        val generator = event.generator
        val output = generator.packOutput
        val existingFileHelper = event.existingFileHelper
        val lookupProvider = event.lookupProvider

        generator.addProvider(event.includeClient(), TechariumLanguageProvider(output, "en_us"))
        generator.addProvider(event.includeClient(), TechariumItemModelProvider(output, existingFileHelper))
        val blockTagProvider = TechariumBlockTagProvider(output, lookupProvider, existingFileHelper)
        generator.addProvider(event.includeServer(), blockTagProvider)
        generator.addProvider(event.includeServer(), TechariumItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper))
        generator.addProvider(event.includeServer(), TechariumRecipeProvider(output, lookupProvider))
        generator.addProvider(event.includeClient(), TechariumSoundProvider(output, existingFileHelper))
        generator.addProvider(event.includeClient(), TechariumBlockStateProvider(output, existingFileHelper))
        generator.addProvider(event.includeServer(), LootTableProvider(output, setOf(),
            listOf(LootTableProvider.SubProviderEntry(::TechariumBlockLootTableProvider, LootContextParamSets.BLOCK)), lookupProvider)
        )

    }
}