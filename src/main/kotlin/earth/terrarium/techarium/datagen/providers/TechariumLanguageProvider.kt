package earth.terrarium.techarium.datagen.providers

import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.registries.ModBlocks
import earth.terrarium.techarium.common.registries.ModItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.neoforged.neoforge.common.data.LanguageProvider

class TechariumLanguageProvider(
    output: PackOutput,
    locale: String
) : LanguageProvider(output, TechariumConstants.MOD_ID, locale) {
    override fun addTranslations() {
        for (entry in ModBlocks.registry.entries) {
            addBlock(entry, entry.id.toEnglishTranslation())
        }
        for (entry in ModItems.registry.entries) {
            if (entry.get() is BlockItem) continue
            addItem(entry, entry.id.toEnglishTranslation())
        }
    }

    private fun ResourceLocation.toEnglishTranslation(): String {
        return this.path.split("_").joinToString(" ") { it.replaceFirstChar(Character::toTitleCase) }
    }
}