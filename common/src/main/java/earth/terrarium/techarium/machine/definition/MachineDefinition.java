package earth.terrarium.techarium.machine.definition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record MachineDefinition(List<MachineModuleDefinition> moduleDefinitions, List<Integer> size) {

	public static final Codec<MachineDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			MachineModuleDefinition.CODEC.listOf().fieldOf("modules").forGetter(MachineDefinition::moduleDefinitions),
			Codec.INT.listOf().fieldOf("size").forGetter(MachineDefinition::size)
	).apply(instance, MachineDefinition::new));

	public static final MachineDefinition EMPTY = new MachineDefinition(List.of(), List.of(1,1,1));

}
