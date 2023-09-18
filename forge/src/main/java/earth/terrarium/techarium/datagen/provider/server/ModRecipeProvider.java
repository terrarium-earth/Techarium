package earth.terrarium.techarium.datagen.provider.server;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.registry.ModBlocks;
import earth.terrarium.techarium.common.registry.ModItems;
import earth.terrarium.techarium.common.tags.ModItemTags;
import earth.terrarium.techarium.datagen.builder.BotariumRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            ModItems.CORN.get(),
            Ingredient.of(ModItemTags.FERTILIZERS),
            ModBlocks.CORN.get(),
            new ItemStack(ModItems.CORN.get(), 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.WHEAT_SEEDS,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.WHEAT,
            new ItemStack(Items.WHEAT, 3),
            new ItemStack(Items.WHEAT_SEEDS, 2));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.PUMPKIN_SEEDS,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.PUMPKIN_STEM,
            Items.PUMPKIN.getDefaultInstance(),
            new ItemStack(Items.PUMPKIN_SEEDS, 2));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.MELON_SEEDS,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.MELON_STEM,
            Items.MELON.getDefaultInstance(),
            new ItemStack(Items.MELON_SEEDS, 2));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.BEETROOT_SEEDS,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.BEETROOTS,
            new ItemStack(Items.BEETROOT, 3),
            new ItemStack(Items.BEETROOT_SEEDS, 2));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.CARROT,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.CARROTS,
            new ItemStack(Items.CARROT, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.POTATO,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.POTATOES,
            new ItemStack(Items.POTATO, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.SWEET_BERRIES,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.SWEET_BERRY_BUSH,
            new ItemStack(Items.SWEET_BERRIES, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.COCOA_BEANS,
            Ingredient.of(ItemTags.JUNGLE_LOGS),
            Blocks.COCOA,
            new ItemStack(Items.COCOA_BEANS, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.BAMBOO,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.BAMBOO,
            new ItemStack(Items.BAMBOO, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.SUGAR_CANE,
            Ingredient.of(Items.DIRT, Items.SAND),
            Blocks.SUGAR_CANE,
            new ItemStack(Items.SUGAR_CANE, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.CACTUS,
            Ingredient.of(ItemTags.SAND),
            Blocks.CACTUS,
            new ItemStack(Items.CACTUS, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.KELP,
            Ingredient.of(ItemTags.SAND),
            Blocks.KELP_PLANT,
            new ItemStack(Items.KELP, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.CHORUS_FRUIT,
            Ingredient.of(Blocks.END_STONE),
            Blocks.CHORUS_FLOWER,
            new ItemStack(Items.CHORUS_FRUIT, 2));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.SEA_PICKLE,
            Ingredient.of(ItemTags.SAND),
            Blocks.SEA_PICKLE,
            new ItemStack(Items.SEA_PICKLE, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.0002), null),
            Items.ALLIUM,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.ALLIUM,
            new ItemStack(Items.ALLIUM, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.0002), null),
            Items.AZURE_BLUET,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.AZURE_BLUET,
            new ItemStack(Items.AZURE_BLUET, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.0002), null),
            Items.BLUE_ORCHID,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.BLUE_ORCHID,
            new ItemStack(Items.BLUE_ORCHID, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.0002), null),
            Items.CORNFLOWER,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.CORNFLOWER,
            new ItemStack(Items.CORNFLOWER, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.DANDELION,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.DANDELION,
            new ItemStack(Items.DANDELION, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.0002), null),
            Items.LILY_OF_THE_VALLEY,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.LILY_OF_THE_VALLEY,
            new ItemStack(Items.LILY_OF_THE_VALLEY, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.ORANGE_TULIP,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.ORANGE_TULIP,
            new ItemStack(Items.ORANGE_TULIP, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.OXEYE_DAISY,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.OXEYE_DAISY,
            new ItemStack(Items.OXEYE_DAISY, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.PINK_TULIP,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.PINK_TULIP,
            new ItemStack(Items.PINK_TULIP, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.POPPY,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.POPPY,
            new ItemStack(Items.POPPY, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.RED_TULIP,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.RED_TULIP,
            new ItemStack(Items.RED_TULIP, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.WHITE_TULIP,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.WHITE_TULIP,
            new ItemStack(Items.WHITE_TULIP, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.WITHER_ROSE,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.WITHER_ROSE,
            new ItemStack(Items.WITHER_ROSE, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.SUNFLOWER,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.SUNFLOWER,
            new ItemStack(Items.SUNFLOWER, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.LILAC,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.LILAC,
            new ItemStack(Items.LILAC, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.ROSE_BUSH,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.ROSE_BUSH,
            new ItemStack(Items.ROSE_BUSH, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.PEONY,
            Ingredient.of(ModItemTags.FERTILIZERS),
            Blocks.PEONY,
            new ItemStack(Items.PEONY, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.NETHER_WART,
            Ingredient.of(Blocks.SOUL_SAND),
            Blocks.NETHER_WART,
            new ItemStack(Items.NETHER_WART, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.BROWN_MUSHROOM,
            Ingredient.of(ModItemTags.MUSHROOM_FERTILIZERS),
            Blocks.BROWN_MUSHROOM,
            new ItemStack(Items.BROWN_MUSHROOM, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.RED_MUSHROOM,
            Ingredient.of(ModItemTags.MUSHROOM_FERTILIZERS),
            Blocks.RED_MUSHROOM,
            new ItemStack(Items.RED_MUSHROOM, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.CRIMSON_FUNGUS,
            Ingredient.of(Blocks.NETHERRACK),
            Blocks.CRIMSON_FUNGUS,
            new ItemStack(Items.CRIMSON_FUNGUS, 3));

        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            Items.WARPED_FUNGUS,
            Ingredient.of(Blocks.NETHERRACK),
            Blocks.WARPED_FUNGUS,
            new ItemStack(Items.WARPED_FUNGUS, 3));
    }

    public static void createBotarium(Consumer<FinishedRecipe> writer, int cookingtime, int energy, FluidHolder fertilizer, Item seed, Ingredient soil, Block cropBlock, ItemStack resultCrop) {
        ResourceLocation seedId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(seed));
        ResourceLocation resultId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(resultCrop.getItem()));

        var builder = new BotariumRecipeBuilder(fertilizer, Ingredient.of(seed), soil, cropBlock, resultCrop)
            .cookingTime(cookingtime)
            .energy(energy);
        builder.save(writer, new ResourceLocation(Techarium.MOD_ID, "botarium/%s".formatted(resultId.getPath(), seedId.getPath())));
    }

    public static void createBotarium(Consumer<FinishedRecipe> writer, int cookingtime, int energy, FluidHolder fertilizer, Item seed, Ingredient soil, Block cropBlock, ItemStack resultCrop, ItemStack resultSeed) {
        ResourceLocation seedId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(seed));
        ResourceLocation resultId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(resultCrop.getItem()));

        var builder = new BotariumRecipeBuilder(fertilizer, Ingredient.of(seed), soil, cropBlock, resultCrop)
            .cookingTime(cookingtime)
            .energy(energy)
            .resultSeed(resultSeed);
        builder.save(writer, new ResourceLocation(Techarium.MOD_ID, "botarium/%s".formatted(resultId.getPath(), seedId.getPath())));
    }
}
