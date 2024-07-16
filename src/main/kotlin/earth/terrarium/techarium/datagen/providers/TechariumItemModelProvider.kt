package earth.terrarium.techarium.datagen.providers

import earth.terrarium.techarium.common.TechariumConstants
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class TechariumItemModelProvider(output: PackOutput, fileHelper: ExistingFileHelper) : ItemModelProvider(output, TechariumConstants.MOD_ID, fileHelper) {
    override fun registerModels() {

    }
}