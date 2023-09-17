package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.recipes.machines.BotariumRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {
    public static final ResourcefulRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, Techarium.MOD_ID);

    public static final RegistryEntry<CodecRecipeSerializer<BotariumRecipe>> BOTARIUM = RECIPE_SERIALIZERS.register("botarium", () ->
        new CodecRecipeSerializer<>(ModRecipeTypes.BOTARIUM.get(), BotariumRecipe::codec));
}
