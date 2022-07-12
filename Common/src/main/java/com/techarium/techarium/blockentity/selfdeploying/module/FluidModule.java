package com.techarium.techarium.blockentity.selfdeploying.module;

import com.google.common.base.Suppliers;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

/**
 * A module that store a fluid.
 */
public class FluidModule {

	public static final FluidModule EMPTY = new FluidModule(0);

	private Supplier<Fluid> fluid;
	private int maxBucket;
	private int milliBuckets;

	public FluidModule(int maxBucket) {
		this.maxBucket = maxBucket;
		this.milliBuckets = 0;
		this.fluid = () -> Fluids.EMPTY;
	}

	/**
	 * Load the module from the tag.
	 *
	 * @param tag the tag to load the module from.
	 */
	public void load(CompoundTag tag) {
		this.milliBuckets = tag.getInt("milliBuckets");
		this.maxBucket = tag.getInt("maxBucket");
		this.fluid = Suppliers.memoize(() -> Registry.FLUID.get(new ResourceLocation(tag.getString("fluid"))));
	}

	/**
	 * Save the module to the tag.
	 *
	 * @param tag the tag to save the module to.
	 */
	public void save(CompoundTag tag) {
		tag.putInt("milliBuckets", this.milliBuckets);
		tag.putInt("maxBucket", this.maxBucket);
		tag.putString("fluid", Registry.FLUID.getKey(this.fluid.get()).toString());
	}

	/**
	 * Add a certain amount of a fluid.
	 *
	 * @param fluid  the fluid to add.
	 * @param amount the amount to add.
	 * @return how much couldn't be added.
	 */
	public int add(Fluid fluid, int amount) {
		if (this.isEmpty()) {
			this.fluid = () -> fluid;
			this.milliBuckets = amount;
			return 0;
		}
		if (!this.fluid.get().isSame(fluid)) {
			return amount;
		}
		int possible = maxBucket * CommonServices.PLATFORM.getFluidHelper().getBucketVolume() - this.milliBuckets;
		if (possible < amount) {
			this.milliBuckets += possible;
			return amount - possible;
		}
		this.milliBuckets += amount;
		return 0;
	}

	/**
	 * Add one bucket of a fluid.
	 *
	 * @param fluid the fluid to add.
	 * @return how much couldn't be added.
	 */
	public int add(Fluid fluid) {
		return this.add(fluid, CommonServices.PLATFORM.getFluidHelper().getBucketVolume());
	}

	/**
	 * Retrieve a certain amount of a fluid.
	 *
	 * @param fluid  the fluid to retrieve.
	 * @param amount the amount to retrieve.
	 * @return how much could be retrieved.
	 */
	public int retrieve(Fluid fluid, int amount) {
		if (this.isEmpty()) {
			return 0;
		}
		if (!this.fluid.get().isSame(fluid)) {
			return 0;
		}
		if (this.milliBuckets <= amount) {
			int removed = this.milliBuckets;
			this.milliBuckets = 0;
			this.fluid = () -> Fluids.EMPTY;
			return removed;
		}
		this.milliBuckets -= amount;
		return amount;
	}

	/**
	 * Retrieve one bucket of a fluid.
	 *
	 * @param fluid the fluid to retrieve.
	 * @return how much could be retrieved.
	 */
	public int retrieve(Fluid fluid) {
		return this.retrieve(fluid, CommonServices.PLATFORM.getFluidHelper().getBucketVolume());
	}

	/**
	 * Retrieve one bucket of the current fluid.
	 *
	 * @return how much could be retrieved.
	 */
	public int retrieve() {
		return this.retrieve(this.fluid.get(), CommonServices.PLATFORM.getFluidHelper().getBucketVolume());
	}

	/**
	 * @return the current fluid.
	 */
	public Fluid currentFluid() {
		return this.fluid.get();
	}

	/**
	 * @return the current amount of fluid.
	 */
	public int currentAmount() {
		return this.milliBuckets;
	}

	/**
	 * @return the amount of bucket it can contain.
	 */
	public int maxBucket() {
		return this.maxBucket;
	}

	/**
	 * @return if the tank is empty.
	 */
	public boolean isEmpty() {
		return this.milliBuckets == 0 || this.fluid.get().isSame(Fluids.EMPTY);
	}

	/**
	 * @param amount the amount of bucket.
	 * @return true if amount bucket can be retrieved.
	 */
	public boolean canRetrieveBucket(int amount) {
		return this.milliBuckets / CommonServices.PLATFORM.getFluidHelper().getBucketVolume() >= amount;
	}

	/**
	 * @param amount the amount of bucket.
	 * @return true if amount bucket can be added.
	 */
	public boolean canAddBucket(int amount) {
		int maxVolume = CommonServices.PLATFORM.getFluidHelper().getBucketVolume() * this.maxBucket;
		int volumeToAdd = CommonServices.PLATFORM.getFluidHelper().getBucketVolume() * amount;
		return maxVolume - this.milliBuckets >= volumeToAdd;
	}

}
