package earth.terrarium.techarium.datagen.providers

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture

class TechariumRecipeProvider(
    output: PackOutput,
    holderLookup: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, holderLookup) {
    override fun buildRecipes(output: RecipeOutput) {

    }
}