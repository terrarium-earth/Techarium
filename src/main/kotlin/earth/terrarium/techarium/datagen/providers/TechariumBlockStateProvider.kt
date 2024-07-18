package earth.terrarium.techarium.datagen.providers

import com.teamresourceful.resourcefullibkt.common.id
import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.registries.ModBlocks
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class TechariumBlockStateProvider(
    output: PackOutput,
    fileHelper: ExistingFileHelper
) : BlockStateProvider(output, TechariumConstants.MOD_ID, fileHelper) {

    private fun parentedBlock(block: Block, parent: String) =
        simpleBlock(block, models().withExistingParent(block.id.path, parent))

    private fun blockEntityBlock(block: Block, noTexture: Boolean = false) =
        simpleBlock(block, models().getBuilder(block.id.path).apply {
            if (!noTexture) texture("particle", "block/${block.id.path}")
        })

    override fun registerStatesAndModels() {
        parentedBlock(ModBlocks.stableFarmland, "minecraft:block/farmland_moist")
        blockEntityBlock(ModBlocks.sprinkler)
        blockEntityBlock(ModBlocks.basicDeployChildBlock, true)
    }
}