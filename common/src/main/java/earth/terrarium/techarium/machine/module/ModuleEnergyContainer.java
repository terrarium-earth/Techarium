package earth.terrarium.techarium.machine.module;

import earth.terrarium.botarium.api.Updatable;
import earth.terrarium.botarium.api.energy.SimpleUpdatingEnergyContainer;
import net.minecraft.nbt.CompoundTag;

public class ModuleEnergyContainer extends SimpleUpdatingEnergyContainer {
    private long maxInsert;
    private long maxExtract;

    public ModuleEnergyContainer(Updatable entity, long energyCapacity, long maxInsert, long maxExtract) {
        super(entity, energyCapacity);
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    @Override
    public long maxInsert() {
        return maxInsert;
    }

    @Override
    public long maxExtract() {
        return maxExtract;
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        super.serialize(tag);
        tag.putLong("MaxInsert", this.maxInsert());
        tag.putLong("MaxExtract", this.maxExtract());
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        setEnergy(tag.getLong("Energy"));
        this.maxInsert = tag.getLong("MaxInsert");
        this.maxExtract = tag.getLong("MaxExtract");
    }

    @Override
    public boolean allowsInsertion() {
        return maxInsert > 0;
    }

    @Override
    public boolean allowsExtraction() {
        return maxExtract > 0;
    }

}
