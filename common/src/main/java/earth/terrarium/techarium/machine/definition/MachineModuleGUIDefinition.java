package earth.terrarium.techarium.machine.definition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * Define the GUI display of a module
 *
 * @param x               the x position in the gui
 * @param y               the y position in the gui
 * @param width           the width of the module
 * @param height          the height of the module
 * @param xOffset         the x offset of the texture in the texture file
 * @param yOffset         the y offset of the texture in the texture file
 * @param textureOverride the texture to use instead of the parent texture
 */
public record MachineModuleGUIDefinition(int x, int y, int width, int height, int xOffset, int yOffset,
                                         Optional<ResourceLocation> textureOverride) {

	public static final Codec<MachineModuleGUIDefinition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("x").forGetter(MachineModuleGUIDefinition::x),
			Codec.INT.fieldOf("y").forGetter(MachineModuleGUIDefinition::y),
			Codec.INT.fieldOf("width").forGetter(MachineModuleGUIDefinition::width),
			Codec.INT.fieldOf("height").forGetter(MachineModuleGUIDefinition::height),
			Codec.INT.optionalFieldOf("x-offset", 0).forGetter(MachineModuleGUIDefinition::xOffset),
			Codec.INT.optionalFieldOf("y-offset", 0).forGetter(MachineModuleGUIDefinition::yOffset),
			ResourceLocation.CODEC.optionalFieldOf("texture-override").forGetter(MachineModuleGUIDefinition::textureOverride)
	).apply(instance, MachineModuleGUIDefinition::new));

}
