package earth.terrarium.techarium.registry;

import earth.terrarium.techarium.machine.definition.MachineDefinition;
import earth.terrarium.techarium.machine.definition.MachineGUIDefinition;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.msrandom.extensions.annotations.ImplementedByExtension;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public final class RegistryHelper {

    @ImplementedByExtension
    public static <E extends AbstractContainerMenu> MenuType<E> createMenuType(MenuTypeFactory<E> factory) {
        throw new NotImplementedException("createBlockEntityType was not implemented.");
    }

    @ImplementedByExtension
    public static ResourceKey<Registry<MultiblockStructure>> getMultiblockRegistryKey() {
        throw new NotImplementedException("getMultiblockRegistryKey was not implemented.");
    }

    @ImplementedByExtension
    public static ResourceKey<Registry<MachineDefinition>> getMachineDefinitionRegistryKey() {
        throw new NotImplementedException("getMachineDefinitionRegistryKey was not implemented.");
    }

    @ImplementedByExtension
    public static ResourceKey<Registry<MachineGUIDefinition>> getMachineGUIDefinitionRegistryKey() {
        throw new NotImplementedException("getMachineGUIDefinitionRegistryKey was not implemented.");
    }

    @ImplementedByExtension
    public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
        throw new NotImplementedException("registerCreativeTab was not implemented.");
    }

    @FunctionalInterface
    public interface MenuTypeFactory<T extends AbstractContainerMenu> {

        T create(int id, Inventory inventory, FriendlyByteBuf buf);

    }
}
