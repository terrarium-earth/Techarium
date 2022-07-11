package com.techarium.techarium.multiblock;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.block.multiblock.MachineCoreBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import com.techarium.techarium.blockentity.selfdeploying.SelfDeployingMultiBlockBlockEntity;
import com.techarium.techarium.registry.TechariumBlocks;
import com.techarium.techarium.util.BlockRegion;
import com.techarium.techarium.util.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
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
public class MultiBlockStructure {

	public static final Codec<List<List<String>>> PATTERN_CODEC = Codec.STRING.listOf().listOf().comapFlatMap(MultiBlockStructure::readPattern, lists -> lists);
	public static final Codec<MultiBlockStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("deployed").forGetter(multiBlockStructure -> toRL(multiBlockStructure.selfDeployingBlock)),
			PATTERN_CODEC.fieldOf("pattern").forGetter(MultiBlockStructure::getPattern),
			Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).fieldOf("keys").forGetter(MultiBlockStructure::getKeys)
	).apply(instance, (selfDeployingBlock1, pattern1, keys1) -> {
		MultiBlockStructure multiBlockStructure = new MultiBlockStructure(selfDeployingBlock1, pattern1, keys1);
		System.out.println(multiBlockStructure);
		return multiBlockStructure;
	}));

	private final Map<BlockPos, Block> positions;
	private SelfDeployingBlock selfDeployingBlock;
	private final List<List<String>> pattern;
	private final Map<String, ResourceLocation> keys;

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

	private List<List<String>> getPattern() {
		return this.pattern;
	}

	private Map<String,ResourceLocation> getKeys() {
		return this.keys;
	}

	private static ResourceLocation toRL(Block block) {
		return Registry.BLOCK.getKey(block);
	}

	private static Block toBlock(ResourceLocation rl) {
		return Registry.BLOCK.get(rl);
	}

	public MultiBlockStructure(ResourceLocation selfDeployingBlock, List<List<String>> pattern, Map<String,ResourceLocation> keys) {
		this.pattern = pattern;
		this.selfDeployingBlock = ((SelfDeployingBlock) toBlock(selfDeployingBlock));
		this.keys = keys;
		this.positions = new HashMap<>();
		BlockPos core = BlockPos.ZERO;
		// search core position
		all: for (int y = 0; y < pattern.size(); y++) {
			List<String> layer = pattern.get(y);
			for (int z = 0; z < layer.size(); z++) {
				String line = layer.get(z);
				for (int x = 0; x < line.length(); x++) {
					if (line.charAt(x) == '@') {


						// 0,0,0 (size 3,2,2) convert to 2,0,0
						// -> size-1-y = 3-1-0 = 2
						// 1,0,0 (size 3,2,2) convert to 1,0,0
						// -> size-1-y = 3-1-1 = 1
						// 2,0,0 (size 3,2,2) convert to 0,0,0
						// -> 3-1-2 = 0
						//:ok_hand: let's go

						// YES IT FUCKING WORKS

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
					String element = "" +line.charAt(x);
					if (keys.containsKey(element)) {
						this.positions.put(new BlockPos(x - core.getX(), pattern.size() - 1 - y - core.getY(), z - core.getZ()), toBlock(keys.get(element)));
					}
				}
			}
		}
		/*
		y
		^  z
		| /
		|/
		+--->x
		 */
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
		for (Map.Entry<BlockPos, Block> entry : this.positions.entrySet()) {
			// first rotate the offset, so we're in the same direction as the core block
			BlockPos offset = MathUtils.rotate(entry.getKey(), direction);
			// now we check if the block at this position in the world match.
			BlockPos levelPos = core.offset(offset);
			Block block = level.getBlockState(levelPos).getBlock();
			if (!entry.getValue().equals(block)) {
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
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		for (BlockPos pos : this.positions.keySet()) {
			// replace multiblock with air so the self-deploying block can safely replace them.
			BlockPos levelPos = corePos.offset(MathUtils.rotate(pos, direction));
			level.setBlock(levelPos, Blocks.AIR.defaultBlockState(), 3);
		}
		level.setBlock(corePos, this.selfDeployingBlock.defaultBlockState(), 3);
		if (level.getBlockEntity(corePos) instanceof SelfDeployingMultiBlockBlockEntity selfDeployingBlockEntity) {
			selfDeployingBlockEntity.deploy();
		}
	}

	/**
	 * Place the blocks of this structure in the world.
	 * This should be called from a self-deploying block being destructed.
	 *
	 * @param level      the world
	 * @param state      the state of the core block of the self-deploying block
	 * @param corePos    the position of the core block of the self-deployed block
	 * @param initiator  the position of the block that initiated the removal of the self-deployed block
	 * @param dropBlocks determine if the blocks of this multiblock should be dropped in the world instead of placed in the world.
	 */
	public void revert(Level level, BlockState state, BlockPos corePos, BlockPos initiator, boolean dropBlocks) {
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		for (Map.Entry<BlockPos, Block> entry : this.positions.entrySet()) {
			BlockPos pos = corePos.offset(MathUtils.rotate(entry.getKey(), direction));
			level.setBlock(pos, entry.getValue().defaultBlockState(), 3);
			if (dropBlocks) {
				level.destroyBlock(pos, true);
			}
		}
		level.setBlock(corePos, TechariumBlocks.MACHINE_CORE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), 3);
		if (dropBlocks) {
			level.destroyBlock(corePos, true);
		}
		level.destroyBlock(initiator, true);
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
		BlockRegion region = this.selfDeployingBlock.getDeployedSize();
		for (int x = region.xOffset; x < region.xSize - region.xOffset; x++) {
			for (int y = region.yOffset; y < region.ySize - region.yOffset; y++) {
				for (int z = region.zOffset; z < region.zSize - region.zOffset; z++) {
					BlockPos offset = new BlockPos(x, y, z);
					BlockPos rotated = MathUtils.rotate(offset, direction);
					BlockState state = level.getBlockState(corePos.offset(rotated));
					if (!(state.getMaterial().isReplaceable() || state.getBlock() instanceof MachineCoreBlock || state.getBlock().equals(this.positions.get(offset)))) {
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
		return "MultiBlockStructure{" +
				"selfDeployingBlock=" + selfDeployingBlock +
				", positions=" + positions.entrySet().stream().map(entry -> "" + entry.getKey().toString() + "->" + entry.getValue() + ",").toList() +
				", keys=" + Joiner.on(",").withKeyValueSeparator("=").join(this.keys) +
				", pattern=" + pattern +
				'}';
	}

}

