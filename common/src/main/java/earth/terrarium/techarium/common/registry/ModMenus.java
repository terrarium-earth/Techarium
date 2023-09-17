package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.botarium.common.registry.RegistryHelpers;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.common.menus.machines.BotariumMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {
    public static final ResourcefulRegistry<MenuType<?>> MENUS = ResourcefulRegistries.create(BuiltInRegistries.MENU, Techarium.MOD_ID);

    public static final RegistryEntry<MenuType<BotariumMenu>> BOTARIUM = MENUS.register("botarium", () -> RegistryHelpers.createMenuType(BotariumMenu::new));
}
