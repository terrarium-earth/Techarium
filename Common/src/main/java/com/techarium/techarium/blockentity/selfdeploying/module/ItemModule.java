package com.techarium.techarium.blockentity.selfdeploying.module;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * A module that store items. Baked by a {@link SimpleContainer}.
 */
public class ItemModule extends SimpleContainer implements InventoryModule<ItemStack> {

	private BlockEntity blockEntity;

	public ItemModule(int size, BlockEntity blockEntity) {
		super(size);
		this.blockEntity = blockEntity;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.ITEM;
	}

	@Override
	public int getSize() {
		return this.getContainerSize();
	}

	@Override
	public ItemStack get(int id) {
		return this.getItem(id);
	}

	@Override
	public ItemStack add(ItemStack value) {
		return this.addItem(value);
	}

	@Override
	public void load(CompoundTag tag) {
		// TODO @Ketheroth: 16/06/2022 upon load, slots are filed from 0 and not where there were previously. manually load and store stacks to preserve position
		this.fromTag(tag.getList("items", Tag.TAG_COMPOUND));
	}

	@Override
	public void save(CompoundTag tag) {
		InventoryModule.super.save(tag);
		tag.put("items", this.createTag());
	}

	@Override
	public void setChanged() {
		super.setChanged();
		this.blockEntity.setChanged();
	}

}
