package earth.terrarium.techarium.compilemixins;

import earth.terrarium.techarium.forge.TechariumCreativeModeTab;
import earth.terrarium.techarium.forge.TechariumForge;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import earth.terrarium.techarium.registry.RegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Supplier;

@Mixin(value = RegistryHelper.class, remap = false)
public class RegistryHelperCompileMixin {
    public static final Supplier<IForgeRegistry<MultiblockStructure>> MULTIBLOCK_STRUCTURE_REGISTRY = TechariumForge.MULTIBLOCK_STRUCTURES.makeRegistry(() -> new RegistryBuilder<MultiblockStructure>().dataPackRegistry(MultiblockStructure.CODEC));

    @Overwrite
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelper.BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }

    @Overwrite
    public static <E extends AbstractContainerMenu> MenuType<E> createMenuType(RegistryHelper.MenuTypeFactory<E> factory) {
        return IForgeMenuType.create(factory::create);
    }

    @Overwrite
    public static ResourceKey<Registry<MultiblockStructure>> getMultiblockRegistryKey() {
        return MULTIBLOCK_STRUCTURE_REGISTRY.get().getRegistryKey();
    }

    @Overwrite
    public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
        return new TechariumCreativeModeTab(tab, icon);
    }

}
