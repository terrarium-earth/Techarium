package com.techarium.techarium.forge;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.forge.extensions.ForgeRegistryHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(Techarium.MOD_ID)
public class TechariumForge {

	public TechariumForge() {
		Techarium.init();

		ForgeRegistryHelper.MULTIBLOCK_STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
		GeckoLib.initialize();
	}

}
