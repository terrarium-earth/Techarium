package com.techarium.techarium.blockentity.selfdeploying.module;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * A module that store a fluid.
 */
// TODO @Ketheroth: 17/06/2022 change class to used milli-buckets instead of buckets
// TODO @Ketheroth: 17/06/2022 rework module a little so some methods of this class are moved to the interface
public class FluidModule implements InventoryModule<Fluid> {

	private Fluid fluid;
	private int size;
	private int amount;

	public FluidModule(int size) {
		this.size = size;
		this.fluid = Fluids.EMPTY;
		this.amount = 0;
	}

	public FluidModule(Fluid fluid, int size) {
		this.fluid = fluid;
		this.size = size;
		this.amount = 0;
	}

	@Override
	public ModuleType getType() {
		return ModuleType.FLUID;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	public int getAmount() {
		return this.amount;
	}

	@Override
	public Fluid get(int id) {
		return 0 <= id && id < amount ? this.fluid : Fluids.EMPTY;
	}

	@Override
	public Fluid add(Fluid value) {
		if (this.fluid.isSame(Fluids.EMPTY)) {
			this.fluid = value;
		}
		if (this.fluid.isSame(value)/* && this.amount < this.size*/) {
			this.amount++;
			return Fluids.EMPTY;
		}
		return value;
	}

	@Override
	public void load(CompoundTag tag) {
		this.amount = tag.getInt("amount");
		this.size = tag.getInt("size");
		this.fluid = Registry.FLUID.get(new ResourceLocation(tag.getString("fluid")));
	}

	@Override
	public void save(CompoundTag tag) {
		InventoryModule.super.save(tag);
		tag.putInt("amount", this.amount);
		tag.putString("fluid", Registry.FLUID.getKey(this.fluid).toString());
	}

	public String getFluid() {
		return Registry.FLUID.getKey(this.fluid).toString();
	}

	public void remove(int amount) {
		this.amount -= amount;
		if (this.amount < 0) {
			this.amount = 0;
		}
	}

}
