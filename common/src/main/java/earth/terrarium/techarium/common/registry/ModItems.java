package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ModItems {
    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Techarium.MOD_ID);
    public static final ResourcefulRegistry<Item> BASIC_ITEMS = ResourcefulRegistries.create(ITEMS);
    public static final Supplier<CreativeModeTab> TAB = new ResourcefulCreativeTab(new ResourceLocation(Techarium.MOD_ID, "main"))
        .setItemIcon(() -> ModItems.ALUMINIUM_GEAR.get())
        .addRegistry(ITEMS)
        .build();

    public static final RegistryEntry<Item> BOTARIUM = ITEMS.register("botarium", () -> new BlockItem(ModBlocks.BOTARIUM.get(), new Item.Properties()));

    public static final RegistryEntry<Item> ALUMINIUM_ORE = ITEMS.register("aluminium_ore", () -> new BlockItem(ModBlocks.ALUMINIUM_ORE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LEAD_ORE = ITEMS.register("lead_ore", () -> new BlockItem(ModBlocks.LEAD_ORE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> NICKEL_ORE = ITEMS.register("nickel_ore", () -> new BlockItem(ModBlocks.NICKEL_ORE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ZINC_ORE = ITEMS.register("zinc_ore", () -> new BlockItem(ModBlocks.ZINC_ORE.get(), new Item.Properties()));

    public static final RegistryEntry<Item> COPPER_FACTORY_BLOCK = ITEMS.register("copper_factory_block", () -> new BlockItem(ModBlocks.COPPER_FACTORY_BLOCK.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ALUMINIUM_FACTORY_BLOCK = ITEMS.register("aluminium_factory_block", () -> new BlockItem(ModBlocks.ALUMINIUM_FACTORY_BLOCK.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LEAD_FACTORY_BLOCK = ITEMS.register("lead_factory_block", () -> new BlockItem(ModBlocks.LEAD_FACTORY_BLOCK.get(), new Item.Properties()));
    public static final RegistryEntry<Item> NICKEL_FACTORY_BLOCK = ITEMS.register("nickel_factory_block", () -> new BlockItem(ModBlocks.NICKEL_FACTORY_BLOCK.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ZINC_FACTORY_BLOCK = ITEMS.register("zinc_factory_block", () -> new BlockItem(ModBlocks.ZINC_FACTORY_BLOCK.get(), new Item.Properties()));

    public static final RegistryEntry<Item> COPPER_NUGGET = BASIC_ITEMS.register("copper_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> COPPER_PLATE = BASIC_ITEMS.register("copper_plate", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> COPPER_GEAR = BASIC_ITEMS.register("copper_gear", () -> new Item(new Item.Properties()));

    public static final RegistryEntry<Item> ALUMINIUM_INGOT = BASIC_ITEMS.register("aluminium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> ALUMINIUM_NUGGET = BASIC_ITEMS.register("aluminium_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> ALUMINIUM_PLATE = BASIC_ITEMS.register("aluminium_plate", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> ALUMINIUM_GEAR = BASIC_ITEMS.register("aluminium_gear", () -> new Item(new Item.Properties()));

    public static final RegistryEntry<Item> LEAD_INGOT = BASIC_ITEMS.register("lead_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> LEAD_NUGGET = BASIC_ITEMS.register("lead_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> LEAD_PLATE = BASIC_ITEMS.register("lead_plate", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> LEAD_GEAR = BASIC_ITEMS.register("lead_gear", () -> new Item(new Item.Properties()));

    public static final RegistryEntry<Item> NICKEL_INGOT = BASIC_ITEMS.register("nickel_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> NICKEL_NUGGET = BASIC_ITEMS.register("nickel_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> NICKEL_PLATE = BASIC_ITEMS.register("nickel_plate", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> NICKEL_GEAR = BASIC_ITEMS.register("nickel_gear", () -> new Item(new Item.Properties()));

    public static final RegistryEntry<Item> ZINC_INGOT = BASIC_ITEMS.register("zinc_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> ZINC_NUGGET = BASIC_ITEMS.register("zinc_nugget", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> ZINC_PLATE = BASIC_ITEMS.register("zinc_plate", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> ZINC_GEAR = BASIC_ITEMS.register("zinc_gear", () -> new Item(new Item.Properties()));
}
