package earth.terrarium.techarium.common.utils;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class ItemUtils {
    public static boolean canAddItem(ItemStack input, ItemStack output) {
        return input.isEmpty() || (ItemStack.isSameItemSameTags(input, output) && output.getCount() + input.getCount() <= input.getMaxStackSize());
    }

    public static boolean canAddItem(Container container, ItemStack output, int... slots) {
        for (int slot : slots) {
            ItemStack input = container.getItem(slot);
            if (canAddItem(input, output)) {
                return true;
            }
        }
        return false;
    }

    public static void addItem(Container container, ItemStack output, int... slots) {
        for (int slot : slots) {
            ItemStack input = container.getItem(slot);
            if (input.isEmpty()) {
                container.setItem(slot, output.copy());
                return;
            } else if (canAddItem(input, output)) {
                input.grow(output.getCount());
                return;
            }
        }
    }
}
