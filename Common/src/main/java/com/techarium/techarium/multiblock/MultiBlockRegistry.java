package com.techarium.techarium.multiblock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MultiBlockRegistry {

	private static final Map<String, MultiBlockStructure> MULTIBLOCK_STRUCTURES = new HashMap<>();

	/**
	 * Register a multiblock structure. If there was already a multiblock associated to the id, the old multiblock is replaced with the new.
	 *
	 * @param id        the id of the multiblock structure.
	 * @param structure the multiblock structure to associate with the id.
	 * @return the previous multiblock associated with the id, or null if there was none.
	 */
	public static MultiBlockStructure register(String id, MultiBlockStructure structure) {
		return MULTIBLOCK_STRUCTURES.put(id, structure);
	}

	/**
	 * Get the multiblock structure associated with the id if present.
	 * if there is no multiblock associated, associate the given multiblock to the id and return this one.
	 *
	 * @param id        the id of the multiblock to get.
	 * @param structure the structure to associate the id if there were none.
	 * @return the multiblock associated to the id.
	 */
	public static MultiBlockStructure getOrSet(String id, Supplier<MultiBlockStructure> structure) {
		if (!MULTIBLOCK_STRUCTURES.containsKey(id)) {
			MULTIBLOCK_STRUCTURES.put(id, structure.get());
		}
		return MULTIBLOCK_STRUCTURES.get(id);
	}

	public static final MultiBlockStructure EMPTY = new MultiBlockStructure.Builder().build();

	static {
		MULTIBLOCK_STRUCTURES.put("empty", EMPTY);
	}

}