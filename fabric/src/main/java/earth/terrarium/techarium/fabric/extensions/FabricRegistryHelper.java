package earth.terrarium.techarium.fabric.extensions;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import earth.terrarium.techarium.registry.RegistryHelper;
import earth.terrarium.techarium.util.Utils;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.MappedRegistry;
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
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import java.util.function.Supplier;

@ClassExtension(RegistryHelper.class)
public class FabricRegistryHelper {

	public static final MappedRegistry<MultiblockStructure> MULTIBLOCK_STRUCTURES = FabricRegistryBuilder.createSimple(MultiblockStructure.class, Utils.resourceLocation(Techarium.MOD_ID + "/multiblock")).buildAndRegister();

	@ImplementsBaseElement
	public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(RegistryHelper.BlockEntityFactory<E> factory, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
	}

	@ImplementsBaseElement
	public static <E extends AbstractContainerMenu> MenuType<E> createMenuType(RegistryHelper.MenuTypeFactory<E> factory) {
		return new ExtendedScreenHandlerType<>(factory::create);
	}

	@ImplementsBaseElement
	@SuppressWarnings("unchecked")
	public static ResourceKey<Registry<MultiblockStructure>> getMultiblockRegistryKey() {
		return (ResourceKey<Registry<MultiblockStructure>>) MULTIBLOCK_STRUCTURES.key();
	}

	@ImplementsBaseElement
	public static CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> icon) {
		return FabricItemGroupBuilder.build(tab, icon);
	}

}
