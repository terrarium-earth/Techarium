package com.techarium.techarium.platform.services;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public interface IRegistryHelper {

	<T extends Item> Supplier<T> registerItem(String id, Supplier<T> item);

	<T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block);

	<E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> blockEntity);

	<E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks);

	@FunctionalInterface
	interface BlockEntityFactory<T extends BlockEntity> {
		T create(BlockPos blockPos, BlockState blockState);
	}

	CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon);

}
