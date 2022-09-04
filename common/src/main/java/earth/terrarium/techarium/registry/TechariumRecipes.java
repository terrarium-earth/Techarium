package earth.terrarium.techarium.registry;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import earth.terrarium.botarium.api.RegistryHolder;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.data.recipes.MagnetCompressRecipe;
import earth.terrarium.techarium.data.recipes.MagnetSplitRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class TechariumRecipes {
    public static final RegistryHolder<RecipeType<?>> TYPES = new RegistryHolder<>(Registry.RECIPE_TYPE, Techarium.MOD_ID);

    public static final RegistryHolder<RecipeSerializer<?>> SERIALIZERS = new RegistryHolder<>(Registry.RECIPE_SERIALIZER, Techarium.MOD_ID);

    public static final Supplier<RecipeType<MagnetCompressRecipe>> MAGNET_COMPRESS_RECIPE_TYPE = TYPES.register("magnet_compressing", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "magnet_compressing";
        }
    });
    public static final Supplier<RecipeType<MagnetSplitRecipe>> MAGNET_SPLIT_RECIPE_TYPE = TYPES.register("magnet_splitting", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "magnet_splitting";
        }
    });

    public static final Supplier<RecipeSerializer<MagnetCompressRecipe>> MAGNET_COMPRESS_RECIPE = SERIALIZERS.register("magnet_compressing", () -> new CodecRecipeSerializer<>(MAGNET_COMPRESS_RECIPE_TYPE.get(), MagnetCompressRecipe::codec));
    public static final Supplier<RecipeSerializer<MagnetSplitRecipe>> MAGNET_SPLIT_RECIPE = SERIALIZERS.register("magnet_splitting", () -> new CodecRecipeSerializer<>(MAGNET_SPLIT_RECIPE_TYPE.get(), MagnetSplitRecipe::codec));
}
