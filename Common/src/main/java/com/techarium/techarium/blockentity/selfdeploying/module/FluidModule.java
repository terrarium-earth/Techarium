package com.techarium.techarium.blockentity.selfdeploying.module;

import com.techarium.techarium.platform.CommonServices;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * A module that store a fluid.
 */
public class FluidModule {

	public static final FluidModule EMPTY = new FluidModule(0);

	private Fluid fluid;
	private int maxBucket;
	private long milliBuckets;

	public FluidModule(int maxBucket) {
		this.maxBucket = maxBucket;
		this.milliBuckets = 0;
		this.fluid = Fluids.EMPTY;
	}

	/**
	 * Load the module from the tag.
	 *
	 * @param tag the tag to load the module from.
	 */
	public void load(CompoundTag tag) {
		this.milliBuckets = tag.getLong("milliBuckets");
		this.maxBucket = tag.getInt("maxBucket");
		this.fluid = Registry.FLUID.get(new ResourceLocation(tag.getString("fluid")));
	}

	/**
	 * Save the module to the tag.
	 *
	 * @param tag the tag to save the module to.
	 */
	public void save(CompoundTag tag) {
		tag.putLong("milliBuckets", this.milliBuckets);
		tag.putInt("maxBucket", this.maxBucket);
		tag.putString("fluid", Registry.FLUID.getKey(this.fluid).toString());
	}

	/**
	 * Add a certain amount of a fluid.
	 *
	 * @param fluid  the fluid to add.
	 * @param amount the amount to add.
	 * @return how much couldn't be added.
	 */
	public long add(Fluid fluid, long amount) {
		if (this.isEmpty()) {
			this.fluid = fluid;
			this.milliBuckets = amount;
			return 0;
		}
		if (!this.fluid.isSame(fluid)) {
			return amount;
		}
		long possible = maxBucket * CommonServices.PLATFORM.getFluidHelper().getBucketVolume() - this.milliBuckets;
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
	public long add(Fluid fluid) {
		return this.add(fluid, CommonServices.PLATFORM.getFluidHelper().getBucketVolume());
	}

	/**
	 * Retrieve a certain amount of a fluid.
	 *
	 * @param fluid  the fluid to retrieve.
	 * @param amount the amount to retrieve.
	 * @return how much could be retrieved.
	 */
	public long retrieve(Fluid fluid, long amount) {
		if (this.isEmpty()) {
			return 0;
		}
		if (!this.fluid.isSame(fluid)) {
			return 0;
		}
		if (this.milliBuckets <= amount) {
			long removed = this.milliBuckets;
			this.milliBuckets = 0;
			this.fluid = Fluids.EMPTY;
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
	public long retrieve(Fluid fluid) {
		return this.retrieve(fluid, CommonServices.PLATFORM.getFluidHelper().getBucketVolume());
	}

	/**
	 * Retrieve one bucket of the current fluid.
	 *
	 * @return how much could be retrieved.
	 */
	public long retrieve() {
		return this.retrieve(this.fluid, CommonServices.PLATFORM.getFluidHelper().getBucketVolume());
	}

	/**
	 * @return the current fluid.
	 */
	public Fluid currentFluid() {
		return this.fluid;
	}

	/**
	 * @return the current amount of fluid.
	 */
	public long currentAmount() {
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
		return this.milliBuckets == 0 || this.fluid.isSame(Fluids.EMPTY);
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
		long maxVolume = CommonServices.PLATFORM.getFluidHelper().getBucketVolume() * this.maxBucket;
		long volumeToAdd = CommonServices.PLATFORM.getFluidHelper().getBucketVolume() * amount;
		return maxVolume - this.milliBuckets >= volumeToAdd;
	}

}
