package com.techarium.techarium.multiblock;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.techarium.techarium.block.multiblock.MultiBlockCoreBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import com.techarium.techarium.blockentity.selfdeploying.SelfDeployingMultiBlockBlockEntity;
import com.techarium.techarium.util.BlockRegion;
import com.techarium.techarium.util.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
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

	public static final MultiBlockStructure EMPTY = new MultiBlockStructure.Builder().build();
	public static final Codec<MultiBlockStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("core").forGetter(multiBlockStructure -> toRL(multiBlockStructure.core)),
			ResourceLocation.CODEC.fieldOf("deployed").forGetter(multiBlockStructure -> toRL(multiBlockStructure.selfDeployingBlock)),
			Codec.list(MBElement.CODED).fieldOf("elements").forGetter(multiBlockStructure -> convertMapToList(multiBlockStructure.positions))
	).apply(instance, (core, deployed, elements) -> new Builder()
			.setCore((MultiBlockCoreBlock) toBlock(core))
			.setSelfDeployingBlock((SelfDeployingBlock) toBlock(deployed))
			.addElements(elements)
			.build()));

	private final Map<BlockPos, Block> positions;
	private MultiBlockCoreBlock core;
	private SelfDeployingBlock selfDeployingBlock;

	/**
	 * Simple class to serialize easily an element of the multiblock.
	 *
	 * @param pos   the position of the element.
	 * @param block the element.
	 */
	private record MBElement(BlockPos pos, ResourceLocation block) {

		private static final Codec<MBElement> CODED = RecordCodecBuilder.create(instance -> instance.group(
				BlockPos.CODEC.fieldOf("pos").forGetter(MBElement::pos),
				ResourceLocation.CODEC.fieldOf("block").forGetter(MBElement::block)
		).apply(instance, MBElement::new));

	}

	private static List<MBElement> convertMapToList(Map<BlockPos, Block> positions) {
		return positions.entrySet().stream().collect(ArrayList::new, (acc, entry) -> acc.add(new MBElement(entry.getKey(), toRL(entry.getValue()))), ArrayList::addAll);
	}

	private static ResourceLocation toRL(Block block) {
		return Registry.BLOCK.getKey(block);
	}

	private static Block toBlock(ResourceLocation rl) {
		return Registry.BLOCK.get(rl);
	}

	private MultiBlockStructure() {
		this.positions = new HashMap<>();
		this.core = null;
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
		if (!this.core.equals(level.getBlockState(core).getBlock())) {
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
		level.setBlock(corePos, this.core.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), 3);
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
					if (!(state.getMaterial().isReplaceable() || state.getBlock() instanceof MultiBlockCoreBlock || state.getBlock().equals(this.positions.get(offset)))) {
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
				"core=" + core +
				", positions=" + positions.entrySet().stream().map(entry -> "" + entry.getKey().toString() + "->" + entry.getValue() + ",").toList() +
				", selfDeployingBlock=" + selfDeployingBlock +
				'}';
	}

	public static class Builder {

		private final MultiBlockStructure structure = new MultiBlockStructure();

		public MultiBlockStructure.Builder setCore(MultiBlockCoreBlock core) {
			this.structure.core = core;
			return this;
		}

		public MultiBlockStructure.Builder setSelfDeployingBlock(SelfDeployingBlock selfDeployingBlock) {
			this.structure.selfDeployingBlock = selfDeployingBlock;
			return this;
		}

		public MultiBlockStructure.Builder addElement(BlockPos position, Block element) {
			this.structure.positions.put(position, element);
			return this;
		}

		public MultiBlockStructure.Builder addElement(MultiBlockStructure.MBElement element) {
			this.structure.positions.put(element.pos, toBlock(element.block));
			return this;
		}

		public MultiBlockStructure.Builder addElements(List<MBElement> elements) {
			for (MBElement element : elements) {

				this.structure.positions.put(element.pos, toBlock(element.block));
			}
			return this;
		}

		public MultiBlockStructure build() {
			System.out.println("building " + this.structure);
			return this.structure;
		}

	}

}

