package earth.terrarium.techarium.multiblock;

import com.google.common.base.Joiner;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.techarium.block.entity.selfdeploying.SelfDeployingMultiblockBlockEntity;
import earth.terrarium.techarium.block.multiblock.MachineCoreBlock;
import earth.terrarium.techarium.block.selfdeploying.SelfDeployingBlock;
import earth.terrarium.techarium.registry.TechariumBlocks;
import earth.terrarium.techarium.util.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define a multiblock structure.
 * Positions of the elements are considered offset from the core position, with the core oriented toward south, and in the minecraft axis (south:+z, east:+x).
 * <br>
 * Instances of this class are created via datapack.
 */
public class MultiblockStructure {

    public static final Codec<List<List<String>>> PATTERN_CODEC = Codec.STRING.listOf().listOf().comapFlatMap(MultiblockStructure::readPattern, lists -> lists);
    public static final Codec<MultiblockStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("deployed").forGetter(multiBlockStructure -> toRL(multiBlockStructure.selfDeployingBlock)),
            PATTERN_CODEC.fieldOf("pattern").forGetter(multiblockStructure -> multiblockStructure.pattern),
            Codec.unboundedMap(Codec.STRING, MultiblockElement.CODEC).fieldOf("keys").forGetter(multiblockStructure -> multiblockStructure.keys)
    ).apply(instance, MultiblockStructure::new));

    private final Map<BlockPos, MultiblockElement> positions;
    private final SelfDeployingBlock selfDeployingBlock;
    private final List<List<String>> pattern;
    private final Map<String, MultiblockElement> keys;

    public MultiblockStructure(ResourceLocation selfDeployingBlock, String[][] pattern, Map<String, MultiblockElement> keys) {
        this(selfDeployingBlock, Arrays.stream(pattern).map(array -> Arrays.stream(array).toList()).toList(), keys);
    }

    public MultiblockStructure(ResourceLocation selfDeployingBlock, List<List<String>> pattern, Map<String, MultiblockElement> keys) {
        this.pattern = pattern;
        this.selfDeployingBlock = ((SelfDeployingBlock) toBlock(selfDeployingBlock));
        this.keys = keys;
        this.positions = new HashMap<>();
        BlockPos core = BlockPos.ZERO;
        // search core position
        all:
        for (int y = 0; y < pattern.size(); y++) {
            List<String> layer = pattern.get(y);
            for (int z = 0; z < layer.size(); z++) {
                String line = layer.get(z);
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == '@') {
                        core = new BlockPos(x, pattern.size() - 1 - y, z);
                        break all;
                    }
                }
            }
        }
        // determine offsets from core
        for (int y = pattern.size() - 1; y >= 0; y--) {
            List<String> layer = pattern.get(y);
            for (int z = 0; z < layer.size(); z++) {
                String line = layer.get(z);
                for (int x = 0; x < line.length(); x++) {
                    String element = "" + line.charAt(x);
                    if (keys.containsKey(element)) {
                        this.positions.put(new BlockPos(x - core.getX(), pattern.size() - 1 - y - core.getY(), z - core.getZ()), keys.get(element));
                    }
                }
            }
        }
    }

    private static DataResult<List<List<String>>> readPattern(List<List<String>> pattern) {
        if (pattern.size() == 0) {
            return DataResult.error("Invalid size");
        }
        int height = pattern.get(0).size();
        if (height == 0) {
            return DataResult.error("Invalid size");
        }
        // every list must have the same size (height of the pattern)
        for (List<String> list : pattern) {
            if (list.size() != height) {
                return DataResult.error("Invalid size");
            }
        }

        int width = pattern.get(0).get(0).length();
        if (width == 0) {
            return DataResult.error("Invalid size");
        }
        // every string must have the same size (width of the pattern)
        for (List<String> list : pattern) {
            for (String element : list) {
                if (element.length() != width) {
                    return DataResult.error("Invalid size");
                }
            }
        }
        for (List<String> layer : pattern) {
            for (String line : layer) {
                if (line.contains("@")) {
                    return DataResult.success(pattern);
                }
            }
        }
        return DataResult.error("@ is not present");
    }

    private static ResourceLocation toRL(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    private static Block toBlock(ResourceLocation rl) {
        return Registry.BLOCK.get(rl);
    }

    /**
     * Determine if a valid structure is present in the world at the core position.
     * The direction should be the direction of the core.
     *
     * @param core      the position of the core
     * @param direction the direction to check the structure
     * @param level     the level to search for the structure.
     * @return true if there is a valid structure in the level.
     */
    public boolean isValidStructure(BlockPos core, Direction direction, Level level) {
        if (!TechariumBlocks.MACHINE_CORE.get().equals(level.getBlockState(core).getBlock())) {
            // if somehow the core in world doesn't match the multiblock core it's not valid
            return false;
        }
        for (Map.Entry<BlockPos, MultiblockElement> entry : this.positions.entrySet()) {
            // first rotate the offset, so we're in the same direction as the core block
            BlockPos offset = MathUtils.rotate(entry.getKey(), direction);
            // now we check if the block at this position in the world match.
            BlockPos levelPos = core.offset(offset);
            if (!entry.getValue().matches(level, levelPos)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Replace the blocks of the structure with the self-deploying blocks.
     *
     * @param level   the level.
     * @param state   the blockstate of the core block.
     * @param corePos the position of the core block.
     */
    public void convert(Level level, BlockState state, BlockPos corePos) {
        if (!this.canConvert(level, state, corePos)) {
            return;
        }
        HashMap<BlockPos, BlockState> states = new HashMap<>();
        Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        for (Map.Entry<BlockPos, MultiblockElement> entry : this.positions.entrySet()) {
            // replace multiblock with air so the self-deploying block can safely replace them.
            BlockPos levelPos = corePos.offset(MathUtils.rotate(entry.getKey(), direction));
            states.put(levelPos, level.getBlockState(levelPos));
            level.setBlock(levelPos, Blocks.AIR.defaultBlockState(), 3);
        }
        level.setBlock(corePos, this.selfDeployingBlock.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), 3);
        if (level.getBlockEntity(corePos) instanceof SelfDeployingMultiblockBlockEntity selfDeployingBlockEntity) {
            selfDeployingBlockEntity.setMultiblockState(states);
            selfDeployingBlockEntity.deploy();
        }
    }

    /**
     * Determine if the structure can be replaced by the deploying block and it can be deployed.
     * The structure can convert if the deploying block replace replaceable blocks, or multiblock block
     *
     * @param level     the level of the multiblock.
     * @param coreState the blockstate of the multiblock core.
     * @param corePos   the position of the multiblock core.
     * @return true if the structure can be deployed.
     */
    public boolean canConvert(Level level, BlockState coreState, BlockPos corePos) {
        Direction direction = coreState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        BoundingBox region = this.selfDeployingBlock.getDeployedSize();
        for (int x = region.minX(); x < region.maxX(); x++) {
            for (int y = region.minY(); y < region.maxY(); y++) {
                for (int z = region.minZ(); z < region.maxZ(); z++) {
                    BlockPos offset = new BlockPos(x, y, z);
                    BlockPos rotated = MathUtils.rotate(offset, direction);
                    BlockState state = level.getBlockState(corePos.offset(rotated));
                    if (!(state.getMaterial().isReplaceable() || state.getBlock() instanceof MachineCoreBlock || this.positions.get(offset).matches(level, corePos.offset(rotated)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @param level the level of the multiblock.
     * @param pos   the position of the multiblock core.
     * @return a list of the blocks that obstruct the self-deploying block to be deployed.
     */
    public List<BlockPos> getObstructingBlocks(Level level, BlockPos pos) {
        return this.selfDeployingBlock.getObstructingBlocks(level, pos);
    }

    @Override
    public String toString() {
        return "MultiblockStructure{" +
                "selfDeployingBlock=" + selfDeployingBlock +
                ", positions=" + positions.entrySet().stream().map(entry -> "" + entry.getKey().toString() + "->" + entry.getValue() + ",").toList() +
                ", keys=" + Joiner.on(",").withKeyValueSeparator("=").join(this.keys) +
                ", pattern=" + pattern +
                '}';
    }

}

