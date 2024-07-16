package earth.terrarium.techarium.datagen.providers

import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.registries.ModBlockEntityTypes
import earth.terrarium.techarium.common.registries.ModBlocks
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class TechariumLanguageProvider(output: PackOutput, locale: String): LanguageProvider(output, TechariumConstants.MOD_ID, locale) {
    override fun addTranslations() {
        for (entry in ModBlocks.registry.entries) {
            addBlock(entry, entry.id.path.split("_")
                .joinToString(" ") { it.replaceFirstChar(Character::toTitleCase) })
        }

        //TODO: Add the translation generation using the item registry.
    }
}