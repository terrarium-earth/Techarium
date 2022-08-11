package com.techarium.techarium;

import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumBlocks;
import com.techarium.techarium.registry.TechariumItems;
import com.techarium.techarium.registry.TechariumMenuTypes;
import com.techarium.techarium.util.PlatformHelper;
import com.techarium.techarium.registry.RegistryHelper;
import com.techarium.techarium.util.Utils;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Techarium {

	public static final String MOD_ID = "techarium";
	public static final Logger LOGGER = LogManager.getLogger();
	public static CreativeModeTab TAB = RegistryHelper.registerCreativeTab(new ResourceLocation(MOD_ID, "tab"), () -> new ItemStack(Blocks.DIAMOND_BLOCK));
	public static final ResourceLocation FONT = Utils.resourceLocation("techarium");
	public static final Style STYLE = Style.EMPTY.withFont(FONT);

	public static final boolean DEBUG_MODE = PlatformHelper.isDevelopmentEnvironment();  // if we want debug items/blocks

	public static void init() {
		TechariumBlocks.REGISTRAR.initialize();
		TechariumItems.REGISTRAR.initialize();
		TechariumBlockEntities.REGISTRAR.initialize();
		TechariumMenuTypes.REGISTRAR.initialize();
	}

}
