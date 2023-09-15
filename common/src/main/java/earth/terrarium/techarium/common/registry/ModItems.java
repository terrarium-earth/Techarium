package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.item.tabs.ResourcefulCreativeTab;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ModItems {
    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Techarium.MOD_ID);
    public static final ResourcefulRegistry<Item> BASIC_ITEMS = ResourcefulRegistries.create(ITEMS);
    public static final Supplier<CreativeModeTab> TAB = new ResourcefulCreativeTab(new ResourceLocation(Techarium.MOD_ID, "main"))
        .setItemIcon(() -> Items.DIRT)
        .addRegistry(ITEMS)
        .build();
}
