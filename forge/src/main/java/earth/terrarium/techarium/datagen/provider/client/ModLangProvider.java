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

        add("tooltip.techarium.energy", "%s ⚡ / %s ⚡");
        add("tooltip.techarium.energy_in", "In: %s ⚡/t");
        add("tooltip.techarium.energy_out", "Out: %s ⚡/t");
        add("tooltip.techarium.max_energy_in", "Max In: %s ⚡/t");
        add("tooltip.techarium.max_energy_out", "Max Out: %s ⚡/t");
        add("tooltip.techarium.energy_use_per_tick", "Uses %s ⚡ per tick");
        add("tooltip.techarium.energy_generation_per_tick", "Generates %s ⚡ per tick");

        add("tooltip.techarium.fluid", "%s 🪣 / %s 🪣 %s");
        add("tooltip.techarium.fluid_in", "In: %s 🪣/t");
        add("tooltip.techarium.fluid_out", "Out: %s 🪣/t");
        add("tooltip.techarium.max_fluid_in", "Max In: %s 🪣/t");
        add("tooltip.techarium.max_fluid_out", "Max Out: %s 🪣/t");
        add("tooltip.techarium.fluid_use_per_iteration", "Uses %s 🪣 per iteration");
        add("tooltip.techarium.fluid_generation_per_iteration", "Generates %s 🪣 per iteration");

        add("tooltip.techarium.ticks_per_iteration", "Takes %s ticks per iteration");

        add(ConstantComponents.CLEAR_FLUID_TANK.getString(), "Shift-right-click to clear");
    }

    public void addFluid(Supplier<? extends Fluid> key, String name) {
        if (key.get() instanceof BotariumFlowingFluid) return;
        add("fluid_type.%s.%s".formatted(Techarium.MOD_ID, Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(key.get())).getPath()), name);
    }
}
