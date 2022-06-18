package com.techarium.techarium.blockentity.selfdeploying.module;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * A module that store items. Baked by a {@link SimpleContainer}.
 */
public class ItemModule extends SimpleContainer {

	public static final ItemModule EMPTY = new ItemModule(0, null);

	private BlockEntity blockEntity;

	public ItemModule(int size, BlockEntity blockEntity) {
		super(size);
		this.blockEntity = blockEntity;
	}

	/**
	 * @return the size of the module.
	 */
	public int getSize() {
		return this.getContainerSize();
	}

	/**
	 * Get an element from the module.
	 *
	 * @param id the id of the element to get.
	 * @return the element.
	 */
	public ItemStack get(int id) {
		return this.getItem(id);
	}

	/**
	 * Add an element to the module.
	 *
	 * @param value the element to add.
	 * @return the remaining element.
	 */
	public ItemStack add(ItemStack value) {
		return this.addItem(value);
	}

	/**
	 * Load the module from the tag.
	 *
	 * @param tag the tag to load the module from.
	 */
	public void load(CompoundTag tag) {
		// TODO @Ketheroth: 16/06/2022 upon load, slots are filed from 0 and not where there were previously. manually load and store stacks to preserve position
		this.fromTag(tag.getList("items", Tag.TAG_COMPOUND));
	}

	/**
	 * Save the module to the tag.
	 *
	 * @param tag the tag to save the module to.
	 */
	public void save(CompoundTag tag) {
		tag.putInt("size", this.getSize());
		tag.put("items", this.createTag());
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.blockEntity.setChanged();
	}

}
