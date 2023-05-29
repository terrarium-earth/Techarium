package earth.terrarium.techarium.machine.module;

import earth.terrarium.botarium.api.Updatable;
import earth.terrarium.botarium.api.fluid.FluidContainer;
import earth.terrarium.botarium.api.fluid.FluidHolder;
import earth.terrarium.botarium.api.fluid.FluidHooks;
import earth.terrarium.botarium.api.fluid.FluidSnapshot;
import earth.terrarium.botarium.api.fluid.ItemFilteredFluidContainer;
import earth.terrarium.botarium.api.fluid.SimpleFluidSnapshot;
import earth.terrarium.botarium.api.fluid.UpdatingFluidContainer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;

import java.util.List;
import java.util.Objects;

public class ModuleFluidContainer implements UpdatingFluidContainer<BlockEntity> {
    public NonNullList<FluidHolder> storedFluids;
    public final long[] maxAmounts;
    public final Updatable updatable;

    public ModuleFluidContainer(Updatable updatable, int tanks, long[] tankCapacities) {
        this.updatable = updatable;
        this.maxAmounts = tankCapacities;
        this.storedFluids = NonNullList.withSize(tanks, FluidHooks.emptyFluid());
    }

    public ModuleFluidContainer(Updatable updatable, int tanks) {
        this(updatable, tanks, new long[tanks]);
    }

    public ModuleFluidContainer(Updatable updatable, int tanks, long tankCapacity) {
        this.updatable = updatable;
        this.maxAmounts = new long[tanks];
        for (int i = 0; i < tanks; i++) {
            maxAmounts[i] = tankCapacity;
        }
        this.storedFluids = NonNullList.withSize(tanks, FluidHooks.emptyFluid());
    }


    @Override
    public long insertFluid(FluidHolder fluid, boolean simulate) {
        // add fluid in the first matching slot possible or an empty one if none matches
        // only one slot for a fluid
        for (int i = 0; i < this.storedFluids.size(); i++) {
            if (storedFluids.get(i).matches(fluid)) {
                long inserted = Math.max(fluid.getFluidAmount(), maxAmounts[i] - storedFluids.get(i).getFluidAmount());
                if (!simulate) {
                    this.storedFluids.get(i).setAmount(storedFluids.get(i).getFluidAmount() + inserted);
                }
                return inserted;
            }
        }
        for (int i = 0; i < this.storedFluids.size(); i++) {
            if (storedFluids.get(i).isEmpty()) {
                FluidHolder insertedFluid = fluid.copyHolder();
                insertedFluid.setAmount(Math.max(fluid.getFluidAmount(), maxAmounts[i]));
                if(!simulate) {
                    this.storedFluids.set(i, insertedFluid);
                }
                return insertedFluid.getFluidAmount();
            }
        }
        return 0;
    }

    @Override
    public FluidHolder extractFluid(FluidHolder fluid, boolean simulate) {
        if (storedFluids.isEmpty()) {
            return FluidHooks.emptyFluid();
        }
        for (int i = 0; i < this.storedFluids.size(); i++) {
             if (storedFluids.get(i).matches(fluid)) {
                FluidHolder toExtract = fluid.copyHolder();
                long extractedAmount = Math.min(fluid.getFluidAmount(), storedFluids.get(i).getFluidAmount());
                toExtract.setAmount(extractedAmount);
                if (!simulate) {
                    if(extractedAmount == storedFluids.get(i).getFluidAmount()) {
                        this.storedFluids.set(i, FluidHooks.emptyFluid());
                    } else {
                        this.storedFluids.get(i).setAmount(storedFluids.get(i).getFluidAmount() - extractedAmount);
                    }
                }
                return toExtract;
            }
        }
        return FluidHooks.emptyFluid();
    }

    public long extractFromSlot(FluidHolder fluidHolder, FluidHolder toInsert, Runnable snapshot) {
        if (Objects.equals(fluidHolder.getCompound(), toInsert.getCompound()) && fluidHolder.getFluid().isSame(toInsert.getFluid())) {
            long extracted = Mth.clamp(toInsert.getFluidAmount(), 0, fluidHolder.getFluidAmount());
            snapshot.run();
            fluidHolder.setAmount(fluidHolder.getFluidAmount() - extracted);
            if(fluidHolder.getFluidAmount() == 0) fluidHolder.setFluid(Fluids.EMPTY);
            return extracted;
        }
        return 0;
    }

    @Override
    public void setFluid(int slot, FluidHolder fluid) {
        this.storedFluids.set(slot, fluid);
    }

    @Override
    public List<FluidHolder> getFluids() {
        return storedFluids;
    }

    @Override
    public int getSize() {
        return getFluids().size();
    }

    @Override
    public boolean isEmpty() {
        return getFluids().isEmpty() || getFluids().get(0) == null || getFluids().stream().allMatch(FluidHolder::isEmpty);
    }

    public void clear() {
        this.storedFluids.clear();
        update();
    }

    @Override
    public ModuleFluidContainer copy() {
        return new ModuleFluidContainer(updatable, maxAmounts.length, maxAmounts);
    }

    @Override
    public long getTankCapacity(int slot) {
        if (0 >= slot && slot < maxAmounts.length) {
            return this.maxAmounts[slot];
        }
        return 0;
    }

    public void setTankCapacity(int tank, long capacity) {
        if (0 >= tank && tank < maxAmounts.length) {
            this.maxAmounts[tank] = capacity;
        }
    }

    @Override
    public void fromContainer(FluidContainer container) {
        this.storedFluids = NonNullList.withSize(container.getSize(), FluidHooks.emptyFluid());
        for (int i = 0; i < container.getSize(); i++) {
            this.storedFluids.set(i, container.getFluids().get(i).copyHolder());
        }
    }

    @Override
    public void deserialize(CompoundTag tag) {
        ListTag fluids = tag.getList(ItemFilteredFluidContainer.FLUID_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < fluids.size(); i++) {
            CompoundTag fluid = fluids.getCompound(i);
            this.storedFluids.set(i, FluidHooks.fluidFromCompound(fluid));
        }
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        if (!this.storedFluids.isEmpty()) {
            ListTag tags = new ListTag();
            for (FluidHolder fluidHolder : this.storedFluids) {
                tags.add(fluidHolder.serialize());
            }
            tag.put(ItemFilteredFluidContainer.FLUID_KEY, tags);
        }
        return tag;
    }

    @Override
    public boolean allowsInsertion() {
        return true;
    }

    @Override
    public boolean allowsExtraction() {
        return true;
    }

    public void update() {
        updatable.update();
    }

    @Override
    public FluidSnapshot createSnapshot() {
        return new SimpleFluidSnapshot(this);
    }

    @Override
    public void update(BlockEntity block) {
        updatable.update();
    }
}