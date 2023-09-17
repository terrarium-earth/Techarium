package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.blocks.machines.BotariumBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, Techarium.MOD_ID);

    public static final ResourcefulRegistry<Block> MACHINES = ResourcefulRegistries.create(BLOCKS);
    public static final ResourcefulRegistry<Block> FLUIDS = ResourcefulRegistries.create(BLOCKS);
    public static final ResourcefulRegistry<Block> CUBES = ResourcefulRegistries.create(BLOCKS);
    public static final ResourcefulRegistry<Block> CUBE_COLUMNS = ResourcefulRegistries.create(BLOCKS);

    public static final RegistryEntry<Block> BOTARIUM = MACHINES.register("botarium", () -> new BotariumBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));

    public static final RegistryEntry<Block> ALUMINIUM_ORE = CUBES.register("aluminium_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryEntry<Block> LEAD_ORE = CUBES.register("lead_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryEntry<Block> NICKEL_ORE = CUBES.register("nickel_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));
    public static final RegistryEntry<Block> ZINC_ORE = CUBES.register("zinc_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)));

    public static final RegistryEntry<Block> COPPER_FACTORY_BLOCK = BLOCKS.register("copper_factory_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ALUMINIUM_FACTORY_BLOCK = BLOCKS.register("aluminium_factory_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> LEAD_FACTORY_BLOCK = BLOCKS.register("lead_factory_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> NICKEL_FACTORY_BLOCK = BLOCKS.register("nickel_factory_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ZINC_FACTORY_BLOCK = BLOCKS.register("zinc_factory_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));

    public static final RegistryEntry<Block> BLOCK_OF_ALUMINIUM = CUBES.register("block_of_aluminium", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> BLOCK_OF_LEAD = CUBES.register("block_of_lead", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> BLOCK_OF_NICKEL = CUBES.register("block_of_nickel", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> BLOCK_OF_ZINC = CUBES.register("block_of_zinc", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));

    public static final RegistryEntry<Block> COPPER_PLATEBLOCK = CUBES.register("copper_plateblock", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ALUMINIUM_PLATEBLOCK = CUBES.register("aluminium_plateblock", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> LEAD_PLATEBLOCK = CUBES.register("lead_plateblock", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> NICKEL_PLATEBLOCK = CUBES.register("nickel_plateblock", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ZINC_PLATEBLOCK = CUBES.register("zinc_plateblock", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));

    public static final RegistryEntry<Block> ENCASED_COPPER_BLOCK = CUBES.register("encased_copper_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ENCASED_ALUMINIUM_BLOCK = CUBES.register("encased_aluminium_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ENCASED_LEAD_BLOCK = CUBES.register("encased_lead_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ENCASED_NICKEL_BLOCK = CUBES.register("encased_nickel_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistryEntry<Block> ENCASED_ZINC_BLOCK = CUBES.register("encased_zinc_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));

    public static final RegistryEntry<Block> METAL_SCAFFOLDING = CUBE_COLUMNS.register("metal_scaffolding", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).noOcclusion()));

    public static final RegistryEntry<Block> ALUMINIUM_LADDER = BLOCKS.register("aluminium_ladder", () -> new LadderBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK).noOcclusion()));
}
