package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.recipes.machines.BotariumRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeTypes {
    public static final ResourcefulRegistry<RecipeType<?>> RECIPE_TYPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_TYPE, Techarium.MOD_ID);

    public static final RegistryEntry<RecipeType<BotariumRecipe>> BOTARIUM = register("botarium");

    private static <T extends Recipe<?>> RegistryEntry<RecipeType<T>> register(String id) {
        return RECIPE_TYPES.register(id, () -> new RecipeType<>() {
            public String toString() {
                return id;
            }
        });
    }
}