package earth.terrarium.techarium.registry;

import earth.terrarium.botarium.api.RegistryHolder;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.data.recipes.MagnetCompressRecipe;
import earth.terrarium.techarium.data.recipes.MagnetSplitRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class TechariumRecipes {
    public static final RegistryHolder<RecipeSerializer<?>> SERIALIZERS = new RegistryHolder<>(Registry.RECIPE_SERIALIZER, Techarium.MOD_ID);

    public static final Supplier<RecipeSerializer<MagnetCompressRecipe>> MAGNET_COMPRESS_RECIPE = SERIALIZERS.register("magnet_compressing", () -> MagnetCompressRecipe.Serializer.INSTANCE);
    public static final Supplier<RecipeSerializer<MagnetSplitRecipe>> MAGNET_SPLIT_RECIPE = SERIALIZERS.register("magnet_splitting", () -> MagnetSplitRecipe.Serializer.INSTANCE);
}
