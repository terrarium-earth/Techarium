package com.techarium.techarium.forge;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.forge.platform.ForgeRegistryHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(Techarium.MOD_ID)
public class TechariumForge {

	public TechariumForge() {
		Techarium.init();

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ForgeRegistryHelper.BLOCKS.register(bus);
		ForgeRegistryHelper.ITEMS.register(bus);
		ForgeRegistryHelper.BLOCK_ENTITIES.register(bus);
		ForgeRegistryHelper.CONTAINERS.register(bus);
		ForgeRegistryHelper.MULTIBLOCK_STRUCTURES.register(bus);
		GeckoLib.initialize();
	}

}