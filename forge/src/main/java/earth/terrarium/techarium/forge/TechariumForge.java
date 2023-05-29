package earth.terrarium.techarium.forge;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.machine.definition.MachineDefinition;
import earth.terrarium.techarium.machine.definition.MachineGUIDefinition;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import software.bernie.geckolib3.GeckoLib;

@Mod(Techarium.MOD_ID)
public class TechariumForge {
    public static final DeferredRegister<MultiblockStructure> MULTIBLOCK_STRUCTURES = DeferredRegister.create(new ResourceLocation(Techarium.MOD_ID, "multiblock"), Techarium.MOD_ID);
	public static final DeferredRegister<MachineDefinition> MACHINE_DEFINITIONS = DeferredRegister.create(new ResourceLocation(Techarium.MOD_ID, "machine"), Techarium.MOD_ID);
	public static final DeferredRegister<MachineGUIDefinition> MACHINE_GUI_DEFINITIONS = DeferredRegister.create(new ResourceLocation(Techarium.MOD_ID, "machine_gui"), Techarium.MOD_ID);

    public TechariumForge() {
        Techarium.init();

        MULTIBLOCK_STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        MACHINE_DEFINITIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        MACHINE_GUI_DEFINITIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        GeckoLib.initialize();
    }

}
