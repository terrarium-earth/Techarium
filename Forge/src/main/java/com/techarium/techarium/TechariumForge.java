package com.techarium.techarium;

import com.techarium.techarium.platform.ForgeRegistryHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Techarium.MOD_ID)
public class TechariumForge {

	public TechariumForge() {
		Techarium.init();

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ForgeRegistryHelper.BLOCKS.register(bus);
		ForgeRegistryHelper.ITEMS.register(bus);
		ForgeRegistryHelper.BLOCK_ENTITIES.register(bus);
	}

}