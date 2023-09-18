package earth.terrarium.techarium.common.recipes.machines;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.IngredientCodec;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.techarium.common.registry.ModRecipeSerializers;
import earth.terrarium.techarium.common.registry.ModRecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public record BotariumRecipe(
    ResourceLocation id,
    int cookingTime, int energy,
    FluidHolder fertilizer,
    Ingredient seed,
    Ingredient soil,
    Block cropBlock,
    ItemStack resultCrop,
    ItemStack resultSeed
) implements CodecRecipe<Container> {

    public static Codec<BotariumRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
            RecordCodecBuilder.point(id),
            Codec.INT.fieldOf("cookingtime").forGetter(BotariumRecipe::cookingTime),
            Codec.INT.fieldOf("energy").forGetter(BotariumRecipe::energy),
            FluidHolder.CODEC.fieldOf("fertilizer").forGetter(BotariumRecipe::fertilizer),
            IngredientCodec.CODEC.fieldOf("seed").forGetter(BotariumRecipe::seed),
            IngredientCodec.CODEC.fieldOf("soil").forGetter(BotariumRecipe::soil),
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("crop_block").forGetter(BotariumRecipe::cropBlock),
            ItemStackCodec.CODEC.fieldOf("result_crop").forGetter(BotariumRecipe::resultCrop),
            ItemStackCodec.CODEC.optionalFieldOf("result_seed", ItemStack.EMPTY).forGetter(BotariumRecipe::resultSeed)
        ).apply(instance, BotariumRecipe::new));
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.BOTARIUM.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.BOTARIUM.get();
    }
}
