package earth.terrarium.techarium.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import earth.terrarium.techarium.Techarium;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MagnetCompressRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final List<ItemStack> outputs;
    private final List<Ingredient> inputs;
    private final int time;

    public MagnetCompressRecipe(ResourceLocation id, List<ItemStack> outputs, List<Ingredient> inputs, int time) {
        this.id = id;
        this.outputs = outputs;
        this.inputs = inputs;
        this.time = time;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, @NotNull Level level) {
        return inputs.stream().noneMatch(input -> container.items.stream().noneMatch(input));
    }

    /**
     * don't use this one because it can have multiple results.
     * use result instead.
     */
    @Override
    public ItemStack assemble(@NotNull SimpleContainer container) {
        return null;
    }

    public List<ItemStack> result(@NotNull SimpleContainer container) {
        return outputs;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<MagnetCompressRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "magnet_compressing";
    }


    public static class Serializer implements RecipeSerializer<MagnetCompressRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Techarium.MOD_ID, "magnet_compressing");


        @Override
        public MagnetCompressRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            JsonArray ingredients = GsonHelper.getAsJsonArray(serializedRecipe, "ingredients");
            List<Ingredient> inputs = new ArrayList<>(Stream.of(ingredients).map(Ingredient::fromJson).toList());
            int time = GsonHelper.getAsInt(serializedRecipe, "craftingTime");
            JsonArray outputArray = GsonHelper.getAsJsonArray(serializedRecipe, "outputs");
            List<ItemStack> outputs = new ArrayList<>(Stream.of(outputArray).map(output -> ShapedRecipe.itemStackFromJson(output.getAsJsonObject())).toList());

            return new MagnetCompressRecipe(recipeId, outputs, inputs, time);
        }

        @Override
        public MagnetCompressRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            List<Ingredient> inputs = new ArrayList<>();
            for (int i = 0; i < buffer.readInt(); i++) {
                inputs.add(Ingredient.fromNetwork(buffer));
            }
            int time = buffer.readInt();
            List<ItemStack> outputs = buffer.readList(FriendlyByteBuf::readNbt).stream().map(ItemStack::of).toList();

            return new MagnetCompressRecipe(recipeId, outputs, inputs, time);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull MagnetCompressRecipe recipe) {
            buffer.writeInt(recipe.inputs.size());
            recipe.inputs.forEach(ingredient -> ingredient.toNetwork(buffer));

            buffer.writeInt(recipe.time);
            buffer.writeCollection(recipe.outputs, (buf, output) -> buf.writeNbt(output.save(new CompoundTag())));
        }
    }
}
