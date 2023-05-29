package earth.terrarium.techarium.forge.extensions;

import earth.terrarium.techarium.forge.TechariumCreativeModeTab;
import earth.terrarium.techarium.forge.TechariumForge;
import earth.terrarium.techarium.machine.definition.MachineDefinition;
import earth.terrarium.techarium.machine.definition.MachineGUIDefinition;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import earth.terrarium.techarium.registry.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

@ClassExtension(RegistryHelper.class)
public class ForgeRegistryHelper {
    public static final Supplier<IForgeRegistry<MultiblockStructure>> MULTIBLOCK_STRUCTURE_REGISTRY = TechariumForge.MULTIBLOCK_STRUCTURES.makeRegistry(() -> new RegistryBuilder<MultiblockStructure>().dataPackRegistry(MultiblockStructure.CODEC));
    public static final Supplier<IForgeRegistry<MachineDefinition>> MACHINE_DEFINITION_REGISTRY = TechariumForge.MACHINE_DEFINITIONS.makeRegistry(() -> new RegistryBuilder<MachineDefinition>().dataPackRegistry(MachineDefinition.CODEC));
    public static final Supplier<IForgeRegistry<MachineGUIDefinition>> MACHINE_GUI_DEFINITION_REGISTRY = TechariumForge.MACHINE_GUI_DEFINITIONS.makeRegistry(() -> new RegistryBuilder<MachineGUIDefinition>().dataPackRegistry(MachineGUIDefinition.CODEC));

    @ImplementsBaseElement
    public static <E extends AbstractContainerMenu> MenuType<E> createMenuType(RegistryHelper.MenuTypeFactory<E> factory) {
        return IForgeMenuType.create(factory::create);
    }

    @ImplementsBaseElement
    public static ResourceKey<Registry<MultiblockStructure>> getMultiblockRegistryKey() {
        return MULTIBLOCK_STRUCTURE_REGISTRY.get().getRegistryKey();
    }

    @ImplementsBaseElement
    public static ResourceKey<Registry<MachineDefinition>> getMachineDefinitionRegistryKey() {
        return MACHINE_DEFINITION_REGISTRY.get().getRegistryKey();
    }

    @ImplementsBaseElement
    public static ResourceKey<Registry<MachineGUIDefinition>> getMachineGUIDefinitionRegistryKey() {
        return MACHINE_GUI_DEFINITION_REGISTRY.get().getRegistryKey();
    }
    @ImplementsBaseElement
    public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
        return new TechariumCreativeModeTab(tab, icon);
    }

}
