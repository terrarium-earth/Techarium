package com.techarium.techarium.multiblock;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MultiBlockRegistry {

	private static final Map<String, MultiBlockStructure> MULTIBLOCK_STRUCTURES = new HashMap<>();

	/**
	 * Register a multiblock structure. If there was already a multiblock associated to the id, the old multiblock is replaced with the new.
	 *
	 * @param structure the multiblock structure to associate with the id.
	 * @return the previous multiblock associated with the id, or null if there was none.
	 */
	public static MultiBlockStructure register(MultiBlockStructure structure) {
		return MULTIBLOCK_STRUCTURES.put(structure.getId(), structure);
	}

	/**
	 * Register the multiblock only if it wasn't registered before.
	 * It is registered with the id of the given multiblock.
	 *
	 * @param structureSupplier the structure to register.
	 * @return the multiblock registered to the id of the given multiblock.
	 */
	public static MultiBlockStructure registerOnce(Supplier<MultiBlockStructure> structureSupplier) {
		MultiBlockStructure structure = structureSupplier.get();
		if (!MULTIBLOCK_STRUCTURES.containsKey(structure.getId())) {
			MULTIBLOCK_STRUCTURES.put(structure.getId(), structure);
		}
		return MULTIBLOCK_STRUCTURES.get(structure.getId());
	}

	/**
	 * Get the multiblock structure associated with the id.
	 *
	 * @param id the id of the multiblock to get.
	 * @return the multiblock associated to the id or null if none.
	 */
	public static MultiBlockStructure get(String id) {
		return MULTIBLOCK_STRUCTURES.getOrDefault(id, EMPTY);
	}

	public static final MultiBlockStructure EMPTY = new MultiBlockStructure.Builder().build();

	static {
		MULTIBLOCK_STRUCTURES.put("empty", EMPTY);
	}

}