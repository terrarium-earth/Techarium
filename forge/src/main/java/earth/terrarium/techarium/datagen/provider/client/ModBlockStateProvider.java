package earth.terrarium.techarium.datagen.provider.client;


import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.registry.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModBlockStateProvider extends BlockStateProvider {
    public static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
    public static final ResourceLocation LADDER = new ResourceLocation("block/ladder");
    public static final ResourceLocation CORN = new ResourceLocation(Techarium.MOD_ID, "block/corn");

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Techarium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.FLUIDS.stream().map(RegistryEntry::get).forEach(this::fluidBlock);
        ModBlocks.CUBES.stream().map(RegistryEntry::get).forEach(this::basicBlock);
        ModBlocks.CUBE_COLUMNS.stream().map(RegistryEntry::get).forEach(this::basicCubeColumn);

        basicRenderedBlock(ModBlocks.BOTARIUM.get(), ModItemModelProvider.SMALL_RENDERED_ITEM);

        basicBlockNoState(ModBlocks.COPPER_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.ALUMINIUM_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.LEAD_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.NICKEL_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.ZINC_FACTORY_BLOCK.get());

        basicLadder(ModBlocks.ALUMINIUM_LADDER.get());
        cornBlock(ModBlocks.CORN.get());
    }

    public void basicBlock(Block block) {
        simpleBlockItem(block, models().getBuilder(name(block)));
        simpleBlock(block);
    }

    public void basicBlock(Block block, ModelFile model) {
        simpleBlockItem(block, models().getBuilder(name(block)));
        simpleBlock(block, model);
    }

    public void basicBlockNoState(Block block) {
        simpleBlockItem(block, models().getBuilder(name(block)));
        cubeAll(block);
    }

    public void basicCubeColumn(Block block) {
        basicBlock(block,
            models().cubeColumn(
                name(block),
                modLoc("block/" + name(block)),
                modLoc("block/" + name(block) + "_top")));
    }

    private void basicLadder(Block block) {
        BlockModelBuilder model = models().getBuilder(name(block))
            .parent(models().getExistingFile(LADDER))
            .texture("texture", modLoc("block/" + name(block)))
            .texture("particle", modLoc("block/" + name(block)));

        getVariantBuilder(block)
            .forAllStates(state -> {
                Direction dir = state.getValue(LadderBlock.FACING);
                return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .build();
            });
    }

    public void basicRenderedBlock(Block block) {
        basicRenderedBlock(block, ModItemModelProvider.RENDERED_ITEM);
    }

    public void basicRenderedBlock(Block block, ResourceLocation itemModel) {
        simpleBlockItem(block, itemModels().getExistingFile(itemModel));
        simpleBlock(block);
    }

    public void basicRenderedBlock(Block block, ResourceLocation itemModel, ResourceLocation texture) {
        simpleBlockItem(block, itemModels().getExistingFile(itemModel));
        simpleBlock(block, this.models().cubeAll(this.name(block), texture));
    }

    public void cornBlock(Block block) {
        this.getVariantBuilder(block)
            .forAllStates(state -> {
                int age = state.getValue(CropBlock.AGE);
                return ConfiguredModel.builder()
                    .modelFile(
                        models().getBuilder(name(block) + "_" + age)
                            .parent(models().getExistingFile(CORN))
                            .texture("stage", modLoc("block/" + name(block) + "/corn_stage_" + age))
                    )
                    .build();
            });
    }

    private void fluidBlock(Block block) {
        simpleBlock(block, models().getBuilder(name(block)).texture("particle", WATER_STILL.toString()));
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private String name(Block block) {
        return this.key(block).getPath();
    }
}
