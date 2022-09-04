package earth.terrarium.techarium.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.util.WorldContainer;
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
import java.util.stream.StreamSupport;

public class MagnetSplitRecipe implements Recipe<WorldContainer> {
    private final ResourceLocation id;
    private final List<ItemStack> outputs;
    private final Ingredient input;
    private final int time;

    public MagnetSplitRecipe(ResourceLocation id, List<ItemStack> outputs, Ingredient input, int time) {
        this.id = id;
        this.outputs = outputs;
        this.input = input;
        this.time = time;
    }

    @Override
    public boolean matches(@NotNull WorldContainer container, @NotNull Level level) {
        return container.getItems().stream().anyMatch(input);
    }

    public int getTime() {
        return time;
    }

    public boolean isValid(ItemStack input){
        return this.input.test(input);
    }

    /**
     * don't use this one because it can have multiple results.
     * use result instead.
     */
    @Override
    public ItemStack assemble(@NotNull WorldContainer container) {
        return ItemStack.EMPTY;
    }

    public List<ItemStack> result(@NotNull WorldContainer container) {
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

    public static class Type implements RecipeType<MagnetSplitRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "magnet_splitting";
    }


    public static class Serializer implements RecipeSerializer<MagnetSplitRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Techarium.MOD_ID, "magnet_splitting");

        @Override
        public MagnetSplitRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject serializedRecipe) {
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(serializedRecipe, "input"));
            int time = GsonHelper.getAsInt(serializedRecipe, "craftingTime");
            JsonArray outputArray = GsonHelper.getAsJsonArray(serializedRecipe, "outputs");
            List<ItemStack> outputs = new ArrayList<>(StreamSupport.stream(outputArray.spliterator(), false).map(output -> ShapedRecipe.itemStackFromJson(output.getAsJsonObject())).toList());

            return new MagnetSplitRecipe(recipeId, outputs, input, time);
        }

        @Override
        public MagnetSplitRecipe fromNetwork(@NotNull ResourceLocation recipeId, @NotNull FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            int time = buffer.readInt();
            List<ItemStack> outputs = buffer.readList(FriendlyByteBuf::readNbt).stream().map(ItemStack::of).toList();

            return new MagnetSplitRecipe(recipeId, outputs, input, time);
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull MagnetSplitRecipe recipe) {
            recipe.input.toNetwork(buffer);
            buffer.writeInt(recipe.time);
            buffer.writeCollection(recipe.outputs, (buf, output) -> buf.writeNbt(output.save(new CompoundTag())));
        }
    }
}
