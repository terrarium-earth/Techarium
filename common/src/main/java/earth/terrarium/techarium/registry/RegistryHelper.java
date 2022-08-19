package earth.terrarium.techarium.registry;

import earth.terrarium.techarium.multiblock.MultiblockStructure;
import earth.terrarium.techarium.util.extensions.ExtendableDeclaration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
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

import java.util.function.Supplier;

@ExtendableDeclaration
public final class RegistryHelper {
    @ExtendableDeclaration
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
        throw new NotImplementedException("createBlockEntityType was not implemented.");
    }

    @ExtendableDeclaration
    public static <E extends AbstractContainerMenu> MenuType<E> createMenuType(MenuTypeFactory<E> factory) {
        throw new NotImplementedException("createBlockEntityType was not implemented.");
    }

    @ExtendableDeclaration
    public static ResourceKey<? extends Registry<MultiblockStructure>> getMultiblockRegistryKey() {
        throw new NotImplementedException("getMultiblockRegistryKey was not implemented.");
    }

    @ExtendableDeclaration
    public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
        throw new NotImplementedException("registerCreativeTab was not implemented.");
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {

        T create(BlockPos blockPos, BlockState blockState);

    }

    @FunctionalInterface
    public interface MenuTypeFactory<T extends AbstractContainerMenu> {

        T create(int id, Inventory inventory, BlockPos pos);

    }
}
