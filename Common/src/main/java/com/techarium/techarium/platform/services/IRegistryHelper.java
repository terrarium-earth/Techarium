package com.techarium.techarium.platform.services;

import com.techarium.techarium.multiblock.MultiBlockStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public interface IRegistryHelper {

	<T extends Item> Supplier<T> registerItem(String id, Supplier<T> item);

	<T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block);

	<E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> blockEntity);

	<E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks);

	<E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenuType(String id, MenuTypeFactory<E> factory);

	MultiBlockStructure getMultiBlockStructure(Level level, ResourceLocation multiBlockStructureId);

	List<ResourceLocation> getMultiBlocksKeys(Level level);

	ResourceLocation getMultiBlockKey(Level level, MultiBlockStructure multiBlockStructure);

	@FunctionalInterface
	interface BlockEntityFactory<T extends BlockEntity> {

		T create(BlockPos blockPos, BlockState blockState);

	}

	@FunctionalInterface
	interface MenuTypeFactory<T extends AbstractContainerMenu> {

		T create(int id, Inventory inventory, BlockPos pos);

	}

	CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon);

}
