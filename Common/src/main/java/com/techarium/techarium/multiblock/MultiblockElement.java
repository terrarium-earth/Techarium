package com.techarium.techarium.multiblock;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MultiblockElement {

	public static final Codec<MultiblockElement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.either(ExtraCodecs.TAG_OR_ELEMENT_ID, ExtraCodecs.TAG_OR_ELEMENT_ID.listOf())
					.xmap(either -> either.map(List::of, right -> right),  // wrap deserialized in a list if it isn't already one
							list -> list.size() == 1 ? Either.left(list.get(0)) : Either.right(list))  // unwrap on serialization if it contains only one element
					.fieldOf("block").forGetter(multiblockElement -> multiblockElement.blocks),
			Codec.STRING.listOf().comapFlatMap(MultiblockElement::readStates, lists -> lists).optionalFieldOf("states").forGetter(multiblockElement -> multiblockElement.states.isEmpty() ? Optional.empty() : Optional.of(multiblockElement.states)),
			CompoundTag.CODEC.optionalFieldOf("nbtTag").forGetter(multiblockElement -> multiblockElement.nbtTag.isEmpty() ? Optional.empty() : Optional.of(multiblockElement.nbtTag))
	).apply(instance, MultiblockElement::new));

	private final List<ExtraCodecs.TagOrElementLocation> blocks;
	private final List<String> states;
	private final CompoundTag nbtTag;

	private MultiblockElement(List<ExtraCodecs.TagOrElementLocation> blocks, List<String> states, CompoundTag nbtTag) {
		// builder use
		this.blocks = blocks;
		this.states = states;
		this.nbtTag = nbtTag;
	}

	private MultiblockElement(List<ExtraCodecs.TagOrElementLocation> blocks, Optional<List<String>> states, Optional<CompoundTag> nbtTag) {
		// codec use
		this.blocks = blocks;
		this.states = states.orElse(List.of());
		this.nbtTag = nbtTag.orElse(new CompoundTag());
	}

	private static DataResult<List<String>> readStates(List<String> states) {
		for (String state : states) {
			if (!state.contains("=") || state.charAt(0) == '=' || state.charAt(state.length() - 1) == '=') {
				return DataResult.error("invalid state definition");
			}
		}
		return DataResult.success(states);
	}

	/**
	 * Determine if the blockstate is valid for this element.
	 * It is valid if it is the same block and every state defined in the element matches.
	 * If multiple value for a property are defined, only one need to match.
	 *
	 * @param level the level of the block to check
	 * @param pos   the position of the block to check
	 * @return true if the blockstate is valid.
	 */
	public boolean matches(Level level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		Block otherBlock = blockState.getBlock();
		ResourceLocation otherKey = Registry.BLOCK.getKey(otherBlock);
		// valid if one of the element in this.blocks matches
		boolean match = false;
		for (ExtraCodecs.TagOrElementLocation validBlock : this.blocks) {
			if (validBlock.tag()) {
				for (Holder<Block> holder : Registry.BLOCK.getTagOrEmpty(TagKey.create(Registry.BLOCK_REGISTRY, validBlock.id()))) {
					if (holder.is(otherKey)) {
						match = true;
						break;
					}
				}
			} else {
				if (validBlock.id().equals(otherKey)) {
					match = true;
					break;
				}
			}
		}
		if (!match) {
			return false;
		}
		// block is valid, now we check its states
		if (!this.states.isEmpty()) {
			Collection<Property<?>> properties = blockState.getProperties();
			List<String[]> splits = this.states.stream().map(state -> state.split("=")).toList();
			for (Property<?> property : properties) {
				String[] propertyValueName = property.value(blockState).toString().split("=");

				List<String[]> sameProperty = splits.stream().filter(split -> split[0].equals(propertyValueName[0])).toList();
				if (sameProperty.isEmpty()) {
					// property not found in the states : valid
					continue;
				}
				if (sameProperty.stream().anyMatch(split -> split[1].equals(propertyValueName[1]))) {
					// one of the value matches the state : valid
					continue;
				}
				// current property value isn't in the list : invalid
				return false;
			}
		}
		//states are valid, now we check its compound tag if there is any
		if (!this.nbtTag.isEmpty()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity != null) {
				return new NbtPredicate(this.nbtTag).matches(blockEntity.saveWithFullMetadata());
			}
		}
		return true;
	}

	public static class Builder {

		private final List<ExtraCodecs.TagOrElementLocation> blocks = new ArrayList<>();
		private final List<String> states = new ArrayList<>();
		private CompoundTag nbtTag = new CompoundTag();

		public Builder block(String resourcelocation) {
			blocks.add(new ExtraCodecs.TagOrElementLocation(new ResourceLocation(resourcelocation), false));
			return this;
		}

		public Builder block(String... resourcelocation) {
			for (String s : resourcelocation) {
				blocks.add(new ExtraCodecs.TagOrElementLocation(new ResourceLocation(s), false));
			}
			return this;
		}

		public Builder tag(String resourcelocation) {
			blocks.add(new ExtraCodecs.TagOrElementLocation(new ResourceLocation(resourcelocation), true));
			return this;
		}

		public Builder tag(String... resourcelocation) {
			for (String s : resourcelocation) {
				blocks.add(new ExtraCodecs.TagOrElementLocation(new ResourceLocation(s), true));
			}
			return this;
		}

		public Builder state(String state) {
			states.add(state);
			return this;
		}

		public Builder state(String... state) {
			states.addAll(Arrays.asList(state));
			return this;
		}

		public Builder nbt(CompoundTag nbtTag) {
			this.nbtTag = nbtTag;
			return this;
		}

		public MultiblockElement build() {
			return new MultiblockElement(blocks, states, nbtTag);
		}

	}

}
