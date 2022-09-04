package earth.terrarium.techarium.data.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public interface SyncedData extends Recipe<Container> {
    @Override
    default boolean matches(@NotNull Container container, @NotNull Level level) {
        return false;
    }

    @Override
    default ItemStack assemble(@NotNull Container container) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canCraftInDimensions(int i, int j) {
        return true;
    }

    @Override
    default ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }
}
