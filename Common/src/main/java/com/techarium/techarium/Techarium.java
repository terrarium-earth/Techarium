package com.techarium.techarium;

import com.techarium.techarium.platform.CommonServices;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumBlocks;
import com.techarium.techarium.registry.TechariumItems;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Techarium {

	public static final String MOD_ID = "techarium";
	public static final String MOD_NAME = "Techarium";
	public static final Logger LOGGER = LogManager.getLogger();
	public static CreativeModeTab TAB = CommonServices.REGISTRY.registerCreativeTab(new ResourceLocation(MOD_ID, "tab"), () -> new ItemStack(Blocks.DIAMOND_BLOCK));
	public static final ResourceLocation FONT = Techarium.resourceLocation("techarium");
	public static final Style STYLE = Style.EMPTY.withFont(FONT);

	public static final boolean DEBUG_MODE = CommonServices.PLATFORM.isDevelopmentEnvironment();  // if we want debug items/blocks

	public static void init() {
		TechariumBlocks.register();
		TechariumItems.register();
		TechariumBlockEntities.register();
		TechariumMenuTypes.register();
	}

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
		return Component.translatable(key).withStyle(STYLE);
	}

}