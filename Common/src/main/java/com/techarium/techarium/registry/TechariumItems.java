package com.techarium.techarium.registry;

import com.techarium.techarium.Techarium;
import com.techarium.techarium.platform.CommonServices;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class TechariumItems {

	public static final Supplier<Item> COMMUNICATION_DEVICE_CORE = CommonServices.REGISTRY.registerItem("com_device_core", () -> new BlockItem(TechariumBlocks.COMMUNICATION_DEVICE_CORE.get(), new Item.Properties().tab(Techarium.TAB)));
	public static final Supplier<Item> COMMUNICATION_DEVICE_ELEMENT = CommonServices.REGISTRY.registerItem("com_device_element", () -> new BlockItem(TechariumBlocks.COMMUNICATION_DEVICE_ELEMENT.get(), new Item.Properties().tab(Techarium.TAB)));

	public static final Supplier<Item> EXCHANGE_STATION = Techarium.DEBUG_MODE ? CommonServices.REGISTRY.registerItem("exchange_station", () -> new BlockItem(TechariumBlocks.EXCHANGE_STATION.get(), new Item.Properties().tab(Techarium.TAB))) : () -> null;

	public static void register() {}

}
