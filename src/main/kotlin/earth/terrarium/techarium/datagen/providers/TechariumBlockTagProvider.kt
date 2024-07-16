package earth.terrarium.techarium.datagen.providers

import earth.terrarium.techarium.common.TechariumConstants
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class TechariumBlockTagProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, fileHelper: ExistingFileHelper): BlockTagsProvider(output, lookupProvider, TechariumConstants.MOD_ID, fileHelper) {
    override fun addTags(holderLookup: HolderLookup.Provider) {

    }
}