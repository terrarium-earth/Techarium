package com.techarium.techarium.platform;

import com.techarium.techarium.Constants;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.platform.services.IRegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Techarium.MOD_ID);
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Techarium.MOD_ID);
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Techarium.MOD_ID);

	@Override
	public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
		return ITEMS.register(id, item);
	}

	@Override
	public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
		return BLOCKS.register(id, block);
	}

	@Override
	public <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> blockEntity) {
		return BLOCK_ENTITIES.register(id, blockEntity);
	}

	@Override
	public <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
		return BlockEntityType.Builder.of(factory::create, blocks).build(null);
	}

	@Override
	public CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
		return new CreativeModeTab(tab.getNamespace() + "." + tab.getPath()) {
			@Override
			public ItemStack makeIcon() {
				return icon.get();
			}
		};
	}

}
