package earth.terrarium.techarium.util;

import earth.terrarium.techarium.Techarium;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class Utils {

	/**
	 * Create a resource location with techarium as namespace.
	 *
	 * @param path the path of the resource location.
	 * @return the resource location.
	 */
	public static ResourceLocation resourceLocation(String path) {
		return new ResourceLocation(Techarium.MOD_ID, path);
	}

	/**
	 * Create a translatable component with the techarium font.
	 *
	 * @param key the translation key.
	 * @return the component.
	 */
	public static MutableComponent translatableComponent(String key) {
		return Component.translatable(key).withStyle(Techarium.STYLE);
	}

}
