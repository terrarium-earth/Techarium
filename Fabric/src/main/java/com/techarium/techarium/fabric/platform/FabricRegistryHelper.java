package com.techarium.techarium.fabric.platform;

import com.mojang.serialization.Lifecycle;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.multiblock.MultiblockStructure;
import com.techarium.techarium.platform.services.IRegistryHelper;
import com.techarium.techarium.util.Utils;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {

	public static final MappedRegistry<MultiblockStructure> MULTIBLOCK_STRUCTURES = FabricRegistryBuilder.createSimple(MultiblockStructure.class, Utils.resourceLocation(Techarium.MOD_ID + "/multiblock")).buildAndRegister();

	static {
		// register our own datapack registries to the builtin registries.
		((WritableRegistry) BuiltinRegistries.REGISTRY).register(MULTIBLOCK_STRUCTURES.key(), MULTIBLOCK_STRUCTURES, Lifecycle.stable());
	}

	@Override
	public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
		T entry = Registry.register(Registry.ITEM, Utils.resourceLocation(id), item.get());
		return () -> entry;
	}

	@Override
	public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
		T entry = Registry.register(Registry.BLOCK, Utils.resourceLocation(id), block.get());
		return () -> entry;
	}

	@Override
	public <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> blockEntity) {
		T entry = Registry.register(Registry.BLOCK_ENTITY_TYPE, Utils.resourceLocation(id), blockEntity.get());
		return () -> entry;
	}

	@Override
	public <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
	}

	@Override
	public <E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenuType(String id, MenuTypeFactory<E> factory) {
		MenuType<E> entry = Registry.register(Registry.MENU, new ResourceLocation(Techarium.MOD_ID, id), new ExtendedScreenHandlerType<>((windowId, inventory, buf) -> factory.create(windowId, inventory, buf.readBlockPos())));
		return () -> entry;
	}

	@Override
	public ResourceKey<? extends Registry<MultiblockStructure>> getMultiblockRegistry() {
		return MULTIBLOCK_STRUCTURES.key();
	}

	@Override
	public CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
		return FabricItemGroupBuilder.build(tab, icon);
	}

}
