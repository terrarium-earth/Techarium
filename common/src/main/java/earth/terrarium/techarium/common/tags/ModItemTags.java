package earth.terrarium.techarium.common.tags;

import earth.terrarium.techarium.Techarium;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    public static final TagKey<Item> FERTILIZERS = tag("fertilizers");
    public static final TagKey<Item> MUSHROOM_FERTILIZERS = tag("mushroom_fertilizers");

    private static TagKey<Item> tag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(Techarium.MOD_ID, name));
    }
}
