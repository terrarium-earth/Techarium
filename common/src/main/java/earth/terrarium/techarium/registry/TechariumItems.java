package earth.terrarium.techarium.registry;

import earth.terrarium.techarium.Techarium;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class TechariumItems {
	public static final TechariumRegistrar<Item> REGISTRAR = new TechariumRegistrar<>(Techarium.MOD_ID, Registry.ITEM);

	public static final Supplier<Item> MACHINE_CORE = REGISTRAR.register("machine_core", () -> new BlockItem(TechariumBlocks.MACHINE_CORE.get(), new Item.Properties().tab(Techarium.TAB)));
	public static final Supplier<Item> COMMUNICATION_DEVICE_ELEMENT = REGISTRAR.register("com_device_element", () -> new BlockItem(TechariumBlocks.COMMUNICATION_DEVICE_ELEMENT.get(), new Item.Properties().tab(Techarium.TAB)));
	public static final Supplier<Item> BOTARIUM = REGISTRAR.register("botarium", () -> new BlockItem(TechariumBlocks.BOTARIUM.get(), new Item.Properties().tab(Techarium.TAB)));
	public static final Supplier<Item> GRAVMAGNET = REGISTRAR.register("gravmagnet", () -> new BlockItem(TechariumBlocks.GRAVMAGNET.get(), new Item.Properties().tab(Techarium.TAB)));
	public static final Supplier<Item> EXCHANGE_STATION = Techarium.DEBUG_MODE ? REGISTRAR.register("exchange_station", () -> new BlockItem(TechariumBlocks.EXCHANGE_STATION.get(), new Item.Properties().tab(Techarium.TAB))) : () -> null;

	/**
	 * This is a placeholder item for a tool.
	 */
	public static final Supplier<Item> TECH_TOOL = REGISTRAR.register("tech_tool", () -> new Item(new Item.Properties().tab(Techarium.TAB)));

}
