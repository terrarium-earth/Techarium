package com.techarium.techarium.fabric.extensions;

import com.mojang.serialization.Lifecycle;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.multiblock.MultiblockStructure;
import com.techarium.techarium.registry.RegistryHelper;
import com.techarium.techarium.util.Utils;
import com.techarium.techarium.util.extensions.ExtensionFor;
import com.techarium.techarium.util.extensions.ExtensionImplementation;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

@ExtensionFor(RegistryHelper.class)
public class FabricRegistryHelper {

	public static final MappedRegistry<MultiblockStructure> MULTIBLOCK_STRUCTURES = FabricRegistryBuilder.createSimple(MultiblockStructure.class, Utils.resourceLocation(Techarium.MOD_ID + "/multiblock")).buildAndRegister();

	static {
		// register our own datapack registries to the builtin registries.
		((WritableRegistry) BuiltinRegistries.REGISTRY).register(MULTIBLOCK_STRUCTURES.key(), MULTIBLOCK_STRUCTURES, Lifecycle.stable());
	}

	@ExtensionImplementation
	public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelper.BlockEntityFactory<E> factory, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
	}

	@ExtensionImplementation
	public static <E extends AbstractContainerMenu> MenuType<E> createMenuType(RegistryHelper.MenuTypeFactory<E> factory) {
		return new ExtendedScreenHandlerType<>((windowId, inventory, buf) -> factory.create(windowId, inventory, buf.readBlockPos()));
	}

	@ExtensionImplementation
	public static ResourceKey<? extends Registry<MultiblockStructure>> getMultiblockRegistryKey() {
		return MULTIBLOCK_STRUCTURES.key();
	}

	@ExtensionImplementation
	public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
		return FabricItemGroupBuilder.build(tab, icon);
	}

}