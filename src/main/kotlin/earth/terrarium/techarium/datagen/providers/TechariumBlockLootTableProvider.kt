package earth.terrarium.techarium.datagen.providers

import earth.terrarium.techarium.common.registries.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class TechariumBlockLootTableProvider(provider: HolderLookup.Provider): BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {
    override fun getKnownBlocks(): MutableIterable<Block> {
        return ModBlocks.registry.entries.map { it.get() }.toMutableList()
    }

    override fun generate() {
        add(ModBlocks.basicDeployChildBlock, noDrop())
    }
}