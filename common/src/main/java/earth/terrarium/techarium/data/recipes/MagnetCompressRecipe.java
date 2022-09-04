package earth.terrarium.techarium.data.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.IngredientCodec;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import earth.terrarium.techarium.registry.TechariumRecipes;
import earth.terrarium.techarium.util.WorldContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MagnetCompressRecipe(ResourceLocation id, Ingredient input, int craftingTime, List<ItemStack> outputs, int count) implements SyncedData {

    public static Codec<MagnetCompressRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                IngredientCodec.CODEC.fieldOf("input").forGetter(MagnetCompressRecipe::input),
                Codec.INT.fieldOf("craftingTime").orElse(100).forGetter(MagnetCompressRecipe::craftingTime),
                ItemStackCodec.CODEC.listOf().fieldOf("outputs").forGetter(MagnetCompressRecipe::outputs),
                Codec.INT.fieldOf("count").orElse(1).forGetter(MagnetCompressRecipe::count)
        ).apply(instance, MagnetCompressRecipe::new));
    }

    public static List<MagnetCompressRecipe> getRecipesForStack(ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(TechariumRecipes.MAGNET_COMPRESS_RECIPE_TYPE.get()).stream().filter(recipe -> recipe.input.test(stack)  && recipe.count() <= stack.getCount()).toList();
    }

    public boolean isValid(ItemStack input){
        return this.input.test(input);
    }

    public int getTime() {
        return craftingTime;
    }
    public Ingredient getInput(){
        return input;
    }

    public List<ItemStack> result(@NotNull WorldContainer container) {
        return outputs;
    }

    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    public ResourceLocation getId() {
        return id;
    }


    public RecipeSerializer<?> getSerializer() {
        return TechariumRecipes.MAGNET_COMPRESS_RECIPE.get();
    }

    public RecipeType<?> getType() {
        return TechariumRecipes.MAGNET_COMPRESS_RECIPE_TYPE.get();
    }
}
