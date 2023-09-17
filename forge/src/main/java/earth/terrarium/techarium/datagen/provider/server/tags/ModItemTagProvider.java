package earth.terrarium.techarium.datagen.provider.server.tags;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends TagsProvider<Item> {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper existingFileHelper) {
        super(output, Registries.ITEM, completableFuture, Techarium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        add(ModItemTags.FERTILIZERS, Items.DIRT);
        add(ModItemTags.FERTILIZERS, Items.GRASS_BLOCK);
        add(ModItemTags.FERTILIZERS, Items.FARMLAND);
        add(ModItemTags.FERTILIZERS, Items.PODZOL);
        add(ModItemTags.MUSHROOM_FERTILIZERS, Items.MYCELIUM);
        add(ModItemTags.MUSHROOM_FERTILIZERS, Items.PODZOL);
    }

    private void add(TagKey<Item> tag, Item item) {
        tag(tag).add(TagEntry.element(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item))));
    }
}
