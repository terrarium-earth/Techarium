package earth.terrarium.techarium.datagen.provider.server;

import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.tags.ModItemTags;
import earth.terrarium.techarium.datagen.builder.BotariumRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
        createBotarium(writer, Items.WHEAT_SEEDS, new ItemStack(Items.WHEAT, 3));
        createBotarium(writer, Items.PUMPKIN_SEEDS, Items.PUMPKIN.getDefaultInstance());
        createBotarium(writer, Items.MELON_SEEDS, Items.MELON.getDefaultInstance());
        createBotarium(writer, Items.BEETROOT_SEEDS, new ItemStack(Items.BEETROOT, 3));
        createBotarium(writer, Items.CARROT, 3);
        createBotarium(writer, Items.POTATO, 3);
        createBotarium(writer, Items.SWEET_BERRIES, 3);
        createBotarium(writer, Items.COCOA_BEANS, Ingredient.of(Items.JUNGLE_LOG), 3);
        createBotarium(writer, Items.NETHER_WART, Ingredient.of(Items.SOUL_SAND), 3);
        createBotarium(writer, Items.BAMBOO, 3);
        createBotarium(writer, Items.SUGAR_CANE, 3);
        createBotarium(writer, Items.CACTUS, Ingredient.of(Items.SAND), 3);
        createBotarium(writer, Items.KELP, Ingredient.of(Items.SAND), 3);
        createBotarium(writer, Items.CHORUS_FLOWER, Ingredient.of(Items.END_STONE), 3);
        createBotarium(writer, Items.CHORUS_FRUIT, Ingredient.of(Items.END_STONE), 3);
        createBotarium(writer, Items.SEA_PICKLE, Ingredient.of(Items.SAND), 3);
        createBotarium(writer, Items.ALLIUM, 3);
        createBotarium(writer, Items.AZURE_BLUET, 3);
        createBotarium(writer, Items.BLUE_ORCHID, 3);
        createBotarium(writer, Items.CORNFLOWER, 3);
        createBotarium(writer, Items.DANDELION, 3);
        createBotarium(writer, Items.LILY_OF_THE_VALLEY, 3);
        createBotarium(writer, Items.ORANGE_TULIP, 3);
        createBotarium(writer, Items.OXEYE_DAISY, 3);
        createBotarium(writer, Items.PINK_TULIP, 3);
        createBotarium(writer, Items.POPPY, 3);
        createBotarium(writer, Items.RED_TULIP, 3);
        createBotarium(writer, Items.WHITE_TULIP, 3);
        createBotarium(writer, Items.WITHER_ROSE, 3);
        createBotarium(writer, Items.SUNFLOWER, 3);
        createBotarium(writer, Items.LILAC, 3);
        createBotarium(writer, Items.ROSE_BUSH, 3);
        createBotarium(writer, Items.PEONY, 3);
        createBotarium(writer, Items.BROWN_MUSHROOM, Ingredient.of(ModItemTags.MUSHROOM_FERTILIZERS), 3);
        createBotarium(writer, Items.RED_MUSHROOM, Ingredient.of(ModItemTags.MUSHROOM_FERTILIZERS), 3);
    }

    public static void createBotarium(Consumer<FinishedRecipe> writer, Item seed) {
        createBotarium(writer, seed, seed.getDefaultInstance());
    }

    public static void createBotarium(Consumer<FinishedRecipe> writer, Item seed, int resultAmount) {
        createBotarium(writer, seed, new ItemStack(seed, resultAmount));
    }

    public static void createBotarium(Consumer<FinishedRecipe> writer, Item seed, Ingredient soil, int resultAmount) {
        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            seed,
            soil,
            new ItemStack(seed, resultAmount)
        );
    }

    public static void createBotarium(Consumer<FinishedRecipe> writer, Item seed, ItemStack result) {
        createBotarium(writer, 600, 5,
            FluidHooks.newFluidHolder(Fluids.WATER, FluidHooks.buckets(0.002), null),
            seed,
            Ingredient.of(ModItemTags.FERTILIZERS),
            result
        );
    }

    public static void createBotarium(Consumer<FinishedRecipe> writer, int cookingtime, int energy, FluidHolder fertilizer, Item seed, Ingredient soil, ItemStack result) {
        ResourceLocation seedId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(seed));
        ResourceLocation resultId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.getItem()));

        var builder = new BotariumRecipeBuilder(fertilizer, Ingredient.of(seed), soil, result)
            .cookingTime(cookingtime)
            .energy(energy);
        builder.save(writer, new ResourceLocation(Techarium.MOD_ID, "botarium/%s_from_%s_in_botarium".formatted(resultId.getPath(), seedId.getPath())));
    }
}
