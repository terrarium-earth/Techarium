package earth.terrarium.techarium.datagen.builder;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.techarium.common.recipes.machines.BotariumRecipe;
import earth.terrarium.techarium.common.registry.ModRecipeSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BotariumRecipeBuilder implements RecipeBuilder {
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    private final FluidHolder fertilizer;
    private final Ingredient seed;
    private final Ingredient soil;
    private final Block cropBlock;
    private final ItemStack resultCrop;

    private ItemStack resultSeed = ItemStack.EMPTY;
    private int cookingtime = 600;
    private int energy = 5;

    public BotariumRecipeBuilder(FluidHolder fertilizer, Ingredient seed, Ingredient soil, Block cropBlock, ItemStack resultCrop) {
        this.fertilizer = fertilizer;
        this.seed = seed;
        this.soil = soil;
        this.cropBlock = cropBlock;
        this.resultCrop = resultCrop;
    }

    public BotariumRecipeBuilder cookingTime(int cookingtime) {
        this.cookingtime = cookingtime;
        return this;
    }

    public BotariumRecipeBuilder energy(int energy) {
        this.energy = energy;
        return this;
    }

    public BotariumRecipeBuilder resultSeed(ItemStack resultSeed) {
        this.resultSeed = resultSeed;
        return this;
    }

    @Override
    public @NotNull BotariumRecipeBuilder unlockedBy(@NotNull String criterionName, @NotNull CriterionTriggerInstance criterionTrigger) {
        this.advancement.addCriterion(criterionName, criterionTrigger);
        return this;
    }

    @Override
    public @NotNull BotariumRecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return this.resultCrop.getItem();
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> finishedRecipeConsumer, @NotNull ResourceLocation
        recipeId) {
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
            .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
            .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeId))
            .requirements(RequirementsStrategy.OR);

        finishedRecipeConsumer.accept(new Result(
            recipeId, this.fertilizer,
            this.seed, this.soil,
            this.cropBlock,
            this.resultCrop, this.resultSeed,
            this.cookingtime, this.energy,
            this.advancement, new ResourceLocation(recipeId.getNamespace(), "recipes/botarium/" + recipeId.getPath()))
        );
    }

    public record Result(
        ResourceLocation id,
        FluidHolder fertilizer,
        Ingredient seed, Ingredient soil,
        Block cropBlock,
        ItemStack resultCrop, ItemStack resultSeed,
        int cookingtime, int energy,
        Advancement.Builder advancement, ResourceLocation advancementId
    ) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            BotariumRecipe.codec(id)
                .encodeStart(JsonOps.INSTANCE, new BotariumRecipe(id, cookingtime, energy, fertilizer, seed, soil, cropBlock, resultCrop, resultSeed))
                .resultOrPartial(Constants.LOGGER::error)
                .ifPresent(out ->
                    out.getAsJsonObject().entrySet().forEach(entry -> json.add(entry.getKey(), entry.getValue()))
                );
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ModRecipeSerializers.BOTARIUM.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
