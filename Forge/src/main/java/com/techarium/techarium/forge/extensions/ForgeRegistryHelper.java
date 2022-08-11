package com.techarium.techarium.forge.extensions;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.forge.TechariumCreativeModeTab;
import com.techarium.techarium.multiblock.MultiblockStructure;
import com.techarium.techarium.registry.RegistryHelper;
import com.techarium.techarium.util.extensions.ExtensionFor;
import com.techarium.techarium.util.extensions.ExtensionImplementation;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

@ExtensionFor(RegistryHelper.class)
public class ForgeRegistryHelper {
    public static final DeferredRegister<MultiblockStructure> MULTIBLOCK_STRUCTURES = DeferredRegister.create(new ResourceLocation(Techarium.MOD_ID, "multiblock"), Techarium.MOD_ID);
    public static final Supplier<IForgeRegistry<MultiblockStructure>> MULTIBLOCK_STRUCTURE_REGISTRY = MULTIBLOCK_STRUCTURES.makeRegistry(() -> new RegistryBuilder<MultiblockStructure>().dataPackRegistry(MultiblockStructure.CODEC));

    @ExtensionImplementation
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelper.BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }

    @ExtensionImplementation
    public static <E extends AbstractContainerMenu> MenuType<E> createMenuType(RegistryHelper.MenuTypeFactory<E> factory) {
        return IForgeMenuType.create((windowId, inv, data) -> factory.create(windowId, inv, data.readBlockPos()));
    }

    @ExtensionImplementation
    public static ResourceKey<? extends Registry<MultiblockStructure>> getMultiblockRegistryKey() {
        return MULTIBLOCK_STRUCTURE_REGISTRY.get().getRegistryKey();
    }

    @ExtensionImplementation
    public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
        return new TechariumCreativeModeTab(tab, icon);
    }

}
