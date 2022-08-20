package earth.terrarium.techarium.forge;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import software.bernie.geckolib3.GeckoLib;

@Mod(Techarium.MOD_ID)
public class TechariumForge {
	public static final DeferredRegister<MultiblockStructure> MULTIBLOCK_STRUCTURES = DeferredRegister.create(new ResourceLocation(Techarium.MOD_ID, "multiblock"), Techarium.MOD_ID);

	public TechariumForge() {
		Techarium.init();

		MULTIBLOCK_STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
		GeckoLib.initialize();
	}

}