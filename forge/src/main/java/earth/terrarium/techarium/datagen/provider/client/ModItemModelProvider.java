package earth.terrarium.techarium.datagen.provider.client;


import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModItemModelProvider extends ItemModelProvider {
    public static final ResourceLocation RENDERED_ITEM = new ResourceLocation(Techarium.MOD_ID, "item/rendered_item");
    public static final ResourceLocation SMALL_RENDERED_ITEM = new ResourceLocation(Techarium.MOD_ID, "item/small_rendered_item");

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Techarium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.BASIC_ITEMS.getEntries().stream().map(RegistryEntry::get).forEach(this::basicItem);

        basicBlockItem(ModItems.ALUMINIUM_LADDER.get());
    }

    public ItemModelBuilder basicBlockItem(Item item) {
        return basicBlockItem(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)));
    }

    public ItemModelBuilder basicBlockItem(ResourceLocation item) {
        return getBuilder(item.toString())
            .parent(new ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", new ResourceLocation(item.getNamespace(), "block/" + item.getPath()));
    }
}
