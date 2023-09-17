package earth.terrarium.techarium.datagen.provider.server.tags;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends TagsProvider<Block> {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper existingFileHelper) {
        super(output, Registries.BLOCK, completableFuture, Techarium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        add(BlockTags.CLIMBABLE, ModBlocks.ALUMINIUM_LADDER.get());
    }

    private void add(TagKey<Block> tag, Block block) {
        tag(tag).add(TagEntry.element(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block))));
    }
}
