package com.techarium.techarium.blockentity.selfdeploying.module;

import net.minecraft.nbt.CompoundTag;

/**
 * A module to add to a container to store data.
 *
 * @param <T> the type of the data to store.
 */
public interface InventoryModule<T> {

	/**
	 * @return the type of the module.
	 */
	ModuleType getType();

	/**
	 * @return the size of the module.
	 */
	int getSize();

	/**
	 * Get an element from the module.
	 *
	 * @param id the id of the element to get.
	 * @return the element.
	 */
	T get(int id);

	/**
	 * Add an element to the module.
	 *
	 * @param value the element to add.
	 * @return the remaining element.
	 */
	T add(T value);

	/**
	 * Load the module from the tag.
	 *
	 * @param tag the tag to load the module from.
	 */
	void load(CompoundTag tag);

	/**
	 * Save the module to the tag.
	 *
	 * @param tag the tag to save the module to.
	 */
	default void save(CompoundTag tag) {
		tag.putString("type", this.getType().toString());
		tag.putInt("size", this.getSize());
	}

	/**
	 * An empty module. Here if someone need one.
	 */
	InventoryModule<Integer> EMPTY = new InventoryModule<>() {
		@Override
		public ModuleType getType() {
			return ModuleType.OTHER;
		}

		@Override
		public int getSize() {
			return 0;
		}

		@Override
		public Integer get(int id) {
			return 0;
		}

		@Override
		public Integer add(Integer value) {
			return 0;
		}

		@Override
		public void load(CompoundTag tag) {
		}
	};

}
