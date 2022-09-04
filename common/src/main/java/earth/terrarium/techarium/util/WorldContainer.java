package earth.terrarium.techarium.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    private Level level;
    private List<ItemEntity> entities = new ArrayList<>();

    public WorldContainer(AABB containerSpace, Level level, ItemStack... itemStacks) {
        super(itemStacks);
        this.containerSpace = containerSpace;
        this.level = level;
    }

    public WorldContainer(AABB containerSpace, Level level) {
        super(10);
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

    public void setLevel(Level level){
        this.level = level;
    }

    /**
     * call this every tick. it updates the container, so it has the correct items.
     */
    public void updateContainerAll(){
        var entities = getEntities();
        this.entities.removeIf(Entity::isRemoved);
        for (ItemEntity item : entities) {
            if(((MagnetTarget) item).Techarium$inContainer()){
                continue;
            }
            ((MagnetTarget) item).Techarium$setInContainer(true);
            this.entities.add(item);
            super.addItem(item.getItem());
        }
        this.entities.stream().filter(item -> !entities.contains(item)).forEach(itemEntity -> {
            removeItemDirect(itemEntity.getItem(), itemEntity.getItem().getCount());
            ((MagnetTarget) itemEntity).Techarium$setIsTarget(false);
            ((MagnetTarget) itemEntity).Techarium$setInContainer(false);
            ((MagnetTarget) itemEntity).Techarium$setProgress(0);
        });
        this.getItems().removeIf(item -> entities.stream().map(ItemEntity::getItem).toList().contains(item));
    }

    public List<ItemEntity> getEntities(){
        return level.getEntitiesOfClass(ItemEntity.class, containerSpace);
    }

    public List<ItemEntity> getEntitiesInside(){
        return entities;
    }

    public void updateSingleItem(int index) throws IndexOutOfBoundsException{
        var itemEntity = getEntities().stream().filter(entity -> !((MagnetTarget) entity).Techarium$inContainer()).toList().get(index);
        ((MagnetTarget) itemEntity).Techarium$setInContainer(true);
        entities.add(itemEntity);
        super.addItem(itemEntity.getItem());
    }

    /**
     * Use this to properly add an item to the container and in the world.
     * @param stack the item to add.
     * @return The ItemStack that was added.
     */
    @Override
    public ItemStack addItem(@NotNull ItemStack stack) {
        var entity = new ItemEntity(level, containerSpace.getCenter().x(), containerSpace.getCenter().y(), containerSpace.getCenter().z(), stack);
        level.addFreshEntity(entity);
        entities.add(entity);
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
     * @return the ItemStack that got removed (don't quote me on this It's hard to tell).
     */
    public ItemStack removeItem(ItemStack stack, int amount) {
        List<ItemEntity> entitiesOfClass = getEntities();
        var toReturn = ItemStack.EMPTY;
        for (ItemEntity item : entitiesOfClass) {
            for (int i = 0; i < amount; i++) {
                if (getItems().stream().anyMatch(j -> ItemStack.isSameItemSameTags(stack, j))) {
                    var itemCopy = item.getItem().copy();
                    itemCopy.setCount(item.getItem().getCount()-1);
                    item.setItem(itemCopy);
                    toReturn = super.removeItem(IntStream.range(0, getItems().size()).filter(j -> ItemStack.isSameItemSameTags(getItems().get(j), stack)).findFirst().orElse(-1), 1);
                } else {
                    return toReturn;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public List<ItemStack> getItems(){
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < getContainerSize(); i++) {
            list.add(getItem(i));
        }
        return list;
    }

    /**
     * If you want to add an item you probably want to use removeItem() instead of removeItemDirect().
     * THIS METHOD ONLY REMOVES THE ITEM FROM THE CONTAINER AND NOT FROM THE ACTUAL WORLD.
     * @param stack the ItemStack to remove.
     * @param amount the amount to remove
     * @return the ItemStack that got removed (don't quote me on this It's hard to tell).
     */
    public ItemStack removeItemDirect(ItemStack stack, int amount) {
        var toReturn = ItemStack.EMPTY;
        for (int i = 0; i <= amount; i++) {
            if (getItems().stream().anyMatch(j -> ItemStack.isSameItemSameTags(j, stack))) {
                amount--;
                toReturn = super.removeItem(IntStream.range(0, getItems().size()).filter(j -> ItemStack.isSameItemSameTags(getItems().get(j), stack)).findFirst().orElse(-1), 1);
            } else {
                return toReturn;
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * Use this to properly remove all items from the container and the world.
     * @return The list of itemstacks that got removed.
     */
    public List<ItemStack> removeAllItems() {
        getEntities().forEach(item -> item.remove(Entity.RemovalReason.DISCARDED));
        return super.removeAllItems();
    }

    /**
     * If you want to remove all items you probably want to use removeAllItems() instead of removeAllItemsDirect().
     * THIS METHOD ONLY REMOVES THE ITEM FROM THE CONTAINER AND NOT FROM THE ACTUAL WORLD.
     * @return The list of ItemStacks that got removed.
     */
    public List<ItemStack> removeAllItemsDirect() {
        return super.removeAllItems();
    }
}
