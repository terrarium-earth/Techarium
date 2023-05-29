package earth.terrarium.techarium.machine.definition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

public record MachineModuleDefinition(ModuleType type, int capacity, int inputRate, int outputRate) {

	public static final Codec<MachineModuleDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ModuleType.CODEC.fieldOf("type").forGetter(MachineModuleDefinition::type),
		Codec.INT.fieldOf("capacity").forGetter(MachineModuleDefinition::capacity),
		Codec.INT.fieldOf("input-rate").forGetter(MachineModuleDefinition::inputRate),
		Codec.INT.fieldOf("output-rate").forGetter(MachineModuleDefinition::outputRate)
	).apply(instance, MachineModuleDefinition::new));

    public enum ModuleType implements StringRepresentable {
        ENERGY("energy"),  // int
        ITEM("item"),  // slot
        FLUID("fluid"),  // int
        GAS("gas");

        public static final Codec<ModuleType> CODEC = StringRepresentable.fromEnum(ModuleType::values);
        private final String name;

        ModuleType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
