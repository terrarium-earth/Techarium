package earth.terrarium.techarium.machine.definition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

/**
 * Define the GUI display of a machine
 *
 * @param inventoryX           x position of the player inventory
 * @param inventoryY           y position of the player inventory
 * @param width                width of the texture
 * @param height               height of the texture
 * @param texture              the texture of the GUI
 * @param guiModuleDefinitions the definitions of the module GUIs
 */
public record MachineGUIDefinition(int inventoryX, int inventoryY, int width, int height, ResourceLocation texture,
                                   List<MachineModuleGUIDefinition> guiModuleDefinitions) {

	public static final Codec<MachineGUIDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("inventory-x").forGetter(MachineGUIDefinition::inventoryX),
			Codec.INT.fieldOf("inventory-y").forGetter(MachineGUIDefinition::inventoryY),
			Codec.INT.fieldOf("width").forGetter(MachineGUIDefinition::width),
			Codec.INT.fieldOf("height").forGetter(MachineGUIDefinition::height),
			ResourceLocation.CODEC.fieldOf("texture").forGetter(MachineGUIDefinition::texture),
			MachineModuleGUIDefinition.CODEC.listOf().fieldOf("modules").forGetter(MachineGUIDefinition::guiModuleDefinitions)
	).apply(instance, MachineGUIDefinition::new));

}
