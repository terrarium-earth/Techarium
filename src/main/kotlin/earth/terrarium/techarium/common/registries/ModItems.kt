package earth.terrarium.techarium.common.registries

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry
import com.teamresourceful.resourcefullibkt.common.getValue
import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.items.GeoBlockItem
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item

object ModItems {

    val registry: ResourcefulRegistry<Item> =
        ResourcefulRegistries.create(BuiltInRegistries.ITEM, TechariumConstants.MOD_ID)

    val sprinkler: GeoBlockItem by registry.register("sprinkler") {
        GeoBlockItem(ModBlocks.sprinkler, Item.Properties())
    }
}