package earth.terrarium.techarium.datagen.providers

import com.teamresourceful.resourcefullibkt.common.ResourceLocation
import com.teamresourceful.resourcefullibkt.common.id
import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.registries.ModItems
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

private val ITEM_MODEL = ResourceLocation(TechariumConstants.MOD_ID, "item/item")
private val SMALL_ITEM_MODEL = ResourceLocation(TechariumConstants.MOD_ID, "item/small_item")

class TechariumItemModelProvider(
    output: PackOutput,
    fileHelper: ExistingFileHelper
) : ItemModelProvider(output, TechariumConstants.MOD_ID, fileHelper) {
    override fun registerModels() {
        withExistingParent(ModItems.sprinkler.id.path, SMALL_ITEM_MODEL)
    }
}