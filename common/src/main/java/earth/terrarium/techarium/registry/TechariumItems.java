package earth.terrarium.techarium.registry;

import earth.terrarium.botarium.api.RegistryHolder;
import earth.terrarium.techarium.Techarium;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class TechariumItems {
	public static final RegistryHolder<Item> ITEMS = new RegistryHolder<>(Registry.ITEM, Techarium.MOD_ID);

	public static final Supplier<Item> MACHINE_CORE = ITEMS.register("machine_core", () -> new BlockItem(TechariumBlocks.MACHINE_CORE.get(), new Item.Properties().tab(Techarium.TAB)));
	public static final Supplier<Item> COMMUNICATION_DEVICE_ELEMENT = ITEMS.register("com_device_element", () -> new BlockItem(TechariumBlocks.COMMUNICATION_DEVICE_ELEMENT.get(), new Item.Properties().tab(Techarium.TAB)));
	public static final Supplier<Item> BOTARIUM = ITEMS.register("botarium", () -> new BlockItem(TechariumBlocks.BOTARIUM.get(), new Item.Properties().tab(Techarium.TAB)));

	public static final Supplier<Item> EXCHANGE_STATION = Techarium.DEBUG_MODE ? ITEMS.register("exchange_station", () -> new BlockItem(TechariumBlocks.EXCHANGE_STATION.get(), new Item.Properties().tab(Techarium.TAB))) : () -> null;

	/**
	 * This is a placeholder item for a tool.
	 */
	public static final Supplier<Item> TECH_TOOL = ITEMS.register("tech_tool", () -> new Item(new Item.Properties().tab(Techarium.TAB)));

}
