package earth.terrarium.techarium.util;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

/**
 * A container to be used with item entities in the world
 */
public class WorldContainer extends SimpleContainer {
    /**
     * the space in the world which defines the container. Any items inside this AABB will be "inside" of the container
     */
    private AABB containerSpace;
    private final Level level;

    public WorldContainer(AABB containerSpace, Level level, ItemStack... itemStacks) {
        super(itemStacks);
        this.containerSpace = containerSpace;
        this.level = level;
    }

    public WorldContainer(AABB containerSpace, Level level) {
        super();
        this.containerSpace = containerSpace;
        this.level = level;
    }

    public AABB getContainerSpace() {
        return containerSpace;
    }

    public void setContainerSpace(AABB containerSpace){
        this.containerSpace = containerSpace;
    }

    public Level getLevel() {
        return level;
    }

    /**
     * call this every tick. it updates the container, so it has the correct items.
     */
    public void updateContainer(){
        for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class, containerSpace)) {
            super.addItem(item.getItem());
        }
    }

    /**
     * Use this to properly add an item to the container and in the world.
     * @param stack the item to add.
     * @return The ItemStack that was added.
     */
    @Override
    public ItemStack addItem(@NotNull ItemStack stack) {
        level.addFreshEntity(new ItemEntity(level, containerSpace.getCenter().x(), containerSpace.getCenter().y(), containerSpace.getCenter().z(), stack));
        return super.addItem(stack);
    }

    /**
     * If you want to add an item you probably want to use addItem() instead of addItemDirect().
     * THIS METHOD ONLY ADDS THE ITEM TO THE CONTAINER AND NOT TO THE ACTUAL WORLD.
     * @param stack the ItemStack to add.
     * @return The ItemStack that was added.
     */
    public ItemStack addItemDirect(@NotNull ItemStack stack){
        return super.addItem(stack);
    }

    /**
     * Use this to properly remove an item from the container and the world.
     * @param stack The ItemStack to remove.
     * @param amount The amount to remove.
     * @return the ItemStack that is left (don't quote me on this It's hard to tell).
     */
    public ItemStack removeItem(ItemStack stack, int amount) {
        List<ItemEntity> entitiesOfClass = level.getEntitiesOfClass(ItemEntity.class, containerSpace);
        var toReturn = ItemStack.EMPTY;
        for (ItemEntity item : entitiesOfClass) {
            if (amount > 0 && items.stream().peek(i -> i.copy().setCount(1)).toList().contains(stack) && ItemStack.isSameItemSameTags(stack, item.getItem())) {
                amount--;
                var itemCopy = item.getItem().copy();
                itemCopy.setCount(item.getItem().getCount()-1);
                item.setItem(itemCopy);
                toReturn = super.removeItem(IntStream.range(0, items.size()).filter(i -> ItemStack.isSameItemSameTags(items.get(i), stack)).findFirst().orElse(-1), 1);
            } else {
                return toReturn;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * If you want to add an item you probably want to use removeItem() instead of removeItemDirect().
     * THIS METHOD ONLY REMOVES THE ITEM FROM THE CONTAINER AND NOT FROM THE ACTUAL WORLD.
     * @param stack the ItemStack to remove.
     * @param amount the amount to remove
     * @return the ItemStack that is left (don't quote me on this It's hard to tell).
     */
    public ItemStack removeItemDirect(ItemStack stack, int amount) {
        var toReturn = ItemStack.EMPTY;
        for (int i = 0; i <= amount; i++) {
            if (items.stream().peek(j -> j.copy().setCount(1)).toList().contains(stack)) {
                amount--;
                toReturn = super.removeItem(IntStream.range(0, items.size()).filter(j -> ItemStack.isSameItemSameTags(items.get(j), stack)).findFirst().orElse(-1), 1);
            } else {
                return toReturn;
            }
        }
        return ItemStack.EMPTY;
    }
}
