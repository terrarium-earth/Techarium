package earth.terrarium.techarium.registry;

import net.msrandom.extensions.annotations.ImplementedByExtension;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

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
    public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
        throw new NotImplementedException("registerCreativeTab was not implemented.");
    }

    @FunctionalInterface
    public interface MenuTypeFactory<T extends AbstractContainerMenu> {

        T create(int id, Inventory inventory, FriendlyByteBuf buf);

    }
}
