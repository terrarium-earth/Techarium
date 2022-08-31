package earth.terrarium.techarium.block.entity.selfdeploying;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

/**
 * A container that stores a fluid.
 */
public class SimpleFluidContainer {
	public static final SimpleFluidContainer EMPTY = new SimpleFluidContainer(0);

	// This can be changed if we want to use a different internal representation format
	public static final long BUCKET_CAPACITY = 81000;

	// Currently used for display
	public static final double TO_MILLI_BUCKETS = 1000.0 / BUCKET_CAPACITY;

	private final long capacity;

	private Holder.Reference<Fluid> fluid;
	private long amount;

	public SimpleFluidContainer(long capacity) {
		this(Fluids.EMPTY, capacity, 0);
	}

	public SimpleFluidContainer(Fluid fluid, long capacity, long amount) {
		this.fluid = getHolder(fluid);

		this.capacity = capacity;
		this.amount = amount;
	}

	/**
	 * Load the container from the tag.
	 *
	 * @param tag the tag to load the container from.
	 */
	public SimpleFluidContainer(CompoundTag tag) {
		var key = ResourceKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(tag.getString("Fluid")));
		this.amount = tag.getLong("Amount");
		this.capacity = tag.getLong("Capacity");
		this.fluid = (Holder.Reference<Fluid>) Registry.FLUID.getHolder(key).orElse(getHolder(Fluids.EMPTY));
	}

	/**
	 * Save the container to a tag.
	 *
	 * @return The tag the container is saved in.
	 */
	public CompoundTag save() {
		CompoundTag tag = new CompoundTag();
		tag.putLong("Amount", this.amount);
		tag.putLong("Capacity", this.capacity);
		tag.putString("Fluid", fluid.key().location().toString());

		return tag;
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
			this.fluid = getHolder(fluid);
			this.amount = amount;
			return 0;
		}

		if (!this.fluid.value().isSame(fluid)) {
			return amount;
		}

		long possible = this.capacity - this.amount;
		if (possible < amount) {
			this.amount += possible;
			return amount - possible;
		}

		this.amount += amount;
		return 0;
	}

	/**
	 * Retrieve a certain amount of a fluid.
	 *
	 * @param fluid  the fluid to retrieve.
	 * @param amount the amount to retrieve.
	 * @return how much could be retrieved.
	 */
	public long retrieve(Fluid fluid, long amount) {
		if (this.isEmpty() || !this.fluid.value().isSame(fluid)) {
			return 0;
		}

		if (this.amount <= amount) {
			long removed = this.amount;
			this.amount = 0;
			this.fluid = getHolder(Fluids.EMPTY);
			return removed;
		}

		this.amount -= amount;
		return amount;
	}

	/**
	 * Retrieve one bucket of the current fluid.
	 *
	 * @return how much could be retrieved.
	 */
	public long retrieve(long amount) {
		return this.retrieve(this.fluid.value(), amount);
	}

	/**
	 * @return if the tank is empty.
	 */
	public boolean isEmpty() {
		return this.amount == 0 || this.fluid.value().isSame(Fluids.EMPTY);
	}

	/**
	 * @param amount the amount of fluid.
	 * @return true if amount bucket can be retrieved.
	 */
	public boolean canRetrieve(long amount) {
		return this.amount >= amount;
	}

	/**
	 * @param amount the amount of fluid.
	 * @return true if amount bucket can be added.
	 */
	public boolean canAdd(long amount) {
		return capacity - this.amount >= amount;
	}

	public Fluid getFluid() {
		return fluid.value();
	}

	public long getAmount() {
		return amount;
	}

	private static Holder.Reference<Fluid> getHolder(Fluid fluid) {
		return Registry.FLUID
				.getResourceKey(fluid).flatMap(Registry.FLUID::getHolder)
				.map(holder -> (Holder.Reference<Fluid>) holder)
				.orElseThrow(() -> new IllegalStateException("Unregistered fluid " + fluid));
	}
}
