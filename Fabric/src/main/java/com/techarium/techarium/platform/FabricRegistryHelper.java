package com.techarium.techarium.platform;

import com.mojang.serialization.Lifecycle;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.multiblock.MultiBlockStructure;
import com.techarium.techarium.platform.services.IRegistryHelper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {

	public static final MappedRegistry<MultiBlockStructure> MULTIBLOCK_STRUCTURES = FabricRegistryBuilder.createSimple(MultiBlockStructure.class, new ResourceLocation(Techarium.MOD_ID, Techarium.MOD_ID + "/multiblock")).buildAndRegister();

	static {
		// register our own datapack registries to the builtin registries.
		((WritableRegistry) BuiltinRegistries.REGISTRY).register(MULTIBLOCK_STRUCTURES.key(), MULTIBLOCK_STRUCTURES, Lifecycle.experimental());
	}

	@Override
	public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
		T entry = Registry.register(Registry.ITEM, Techarium.resourceLocation(id), item.get());
		return () -> entry;
	}

	@Override
	public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> block) {
		T entry = Registry.register(Registry.BLOCK, Techarium.resourceLocation(id), block.get());
		return () -> entry;
	}

	@Override
	public <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> blockEntity) {
		T entry = Registry.register(Registry.BLOCK_ENTITY_TYPE, Techarium.resourceLocation(id), blockEntity.get());
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
	public MultiBlockStructure getMultiBlockStructure(Level level, ResourceLocation multiBlockStructureId) {
		if (level == null) {
			return null;
		}
		Optional<? extends Registry<MultiBlockStructure>> registry = level.registryAccess().registry(MULTIBLOCK_STRUCTURES.key());
		return registry.map(multiBlockStructures -> multiBlockStructures.get(multiBlockStructureId)).orElse(null);
	}

	@Override
	public List<ResourceLocation> getMultiBlocksKeys(Level level) {
		return level.registryAccess().registry(MULTIBLOCK_STRUCTURES.key()).map(multiBlockStructures -> multiBlockStructures.keySet().stream().toList()).orElseGet(List::of);
	}

	@Override
	public ResourceLocation getMultiBlockKey(Level level, MultiBlockStructure multiBlockStructure) {
		return level.registryAccess().registry(MULTIBLOCK_STRUCTURES.key()).map(registry ->registry.getKey(multiBlockStructure)).orElse(Techarium.resourceLocation("empty"));
	}

	@Override
	public CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
		return FabricItemGroupBuilder.build(tab, icon);
	}

}
