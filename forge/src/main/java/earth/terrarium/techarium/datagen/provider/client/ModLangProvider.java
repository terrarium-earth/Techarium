package earth.terrarium.techarium.datagen.provider.client;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.botarium.common.registry.fluid.BotariumFlowingFluid;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.constants.ConstantComponents;
import earth.terrarium.techarium.common.registry.ModBlocks;
import earth.terrarium.techarium.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.codehaus.plexus.util.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, Techarium.MOD_ID, "en_us");
    }

    private static final Set<RegistryEntry<Item>> CUSTOM_ITEM_NAMES = Set.of();
    private static final Set<RegistryEntry<Block>> CUSTOM_BLOCK_NAMES = Set.of();

    @Override
    protected void addTranslations() {
        ModBlocks.BLOCKS.stream()
            .filter(b -> !(CUSTOM_BLOCK_NAMES.contains(b)))
            .forEach(entry -> addBlock(entry,
                StringUtils.capitaliseAllWords(entry
                    .getId()
                    .getPath()
                    .replace("_", " "))));

        ModItems.ITEMS.stream()
            .filter(i -> !(i.get() instanceof BlockItem))
            .filter(i -> !(CUSTOM_ITEM_NAMES.contains(i)))
            .forEach(entry -> addItem(entry,
                StringUtils.capitaliseAllWords(entry
                    .getId()
                    .getPath()
                    .replace("_", " "))));

//        ModEntityTypes.ENTITY_TYPES.stream()
//            .forEach(entry -> addEntityType(entry,
//                StringUtils.capitaliseAllWords(entry
//                    .getId()
//                    .getPath()
//                    .replace("_", " "))));
//
//        ModFluids.FLUIDS.stream()
//            .forEach(entry -> addFluid(entry,
//                StringUtils.capitaliseAllWords(entry
//                    .getId()
//                    .getPath()
//                    .replace("_", " "))));

        add(ConstantComponents.ITEM_GROUP.getString(), "Techarium");
    }

    public void addFluid(Supplier<? extends Fluid> key, String name) {
        if (key.get() instanceof BotariumFlowingFluid) return;
        add("fluid_type.%s.%s".formatted(Techarium.MOD_ID, Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(key.get())).getPath()), name);
    }
}
