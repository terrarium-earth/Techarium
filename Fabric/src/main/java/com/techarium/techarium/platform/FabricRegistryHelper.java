package com.techarium.techarium.platform;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.platform.services.IRegistryHelper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {

	@Override
	public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
		T entry = Registry.register(Registry.ITEM, new ResourceLocation(Techarium.MOD_ID, id), item.get());
		return () -> entry;
	}

	@Override
	public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
		T entry = Registry.register(Registry.BLOCK, new ResourceLocation(Techarium.MOD_ID, id), block.get());
		return () -> entry;
	}

	@Override
	public <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> blockEntity) {
		T entry = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Techarium.MOD_ID, id), blockEntity.get());
		return () -> entry;
	}

	@Override
	public <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
	}

	@Override
	public CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
		return FabricItemGroupBuilder.build(tab, icon);
	}

}
