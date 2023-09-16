package earth.terrarium.techarium.datagen.provider.client;


import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockStateProvider extends BlockStateProvider {
    public static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Techarium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.FLUIDS.stream().map(RegistryEntry::get).forEach(this::fluidBlock);
        ModBlocks.CUBES.stream().map(RegistryEntry::get).forEach(this::basicBlock);

        basicBlockNoState(ModBlocks.COPPER_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.ALUMINIUM_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.LEAD_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.NICKEL_FACTORY_BLOCK.get());
        basicBlockNoState(ModBlocks.ZINC_FACTORY_BLOCK.get());
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
