package earth.terrarium.techarium.datagen.providers

import earth.terrarium.techarium.common.TechariumConstants
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider

class TechariumSoundProvider(
    output: PackOutput,
    fileHelper: ExistingFileHelper
) : SoundDefinitionsProvider(output, TechariumConstants.MOD_ID, fileHelper) {
    override fun registerSounds() {

    }
}