package earth.terrarium.techarium.common.items

import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block
import software.bernie.geckolib.animatable.GeoItem
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.util.GeckoLibUtil

class GeoBlockItem(block: Block, properties: Properties) : BlockItem(block, properties), GeoItem {

    private val cache = GeckoLibUtil.createInstanceCache(this)

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {

    }

    override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache
}