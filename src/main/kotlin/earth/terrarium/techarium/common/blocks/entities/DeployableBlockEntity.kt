package earth.terrarium.techarium.common.blocks.entities

import earth.terrarium.techarium.common.blocks.base.TechariumBlockStates.DEPLOYED
import earth.terrarium.techarium.common.blocks.base.Tickable
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import software.bernie.geckolib.animatable.GeoBlockEntity
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache
import software.bernie.geckolib.util.GeckoLibUtil

abstract class DeployableBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos, state: BlockState
) : ComponentBlockEntity(type, pos, state), GeoBlockEntity, Tickable {

    private val cache = GeckoLibUtil.createInstanceCache(this)
    var ticks = 0
        private set

    protected val isDeployed: Boolean
        get() = this.blockState.hasProperty(DEPLOYED) && this.blockState.getValue(DEPLOYED)

    final override fun getAnimatableInstanceCache(): AnimatableInstanceCache = cache

    final override fun tick(level: Level, pos: BlockPos, state: BlockState) {
        ticks++
        super.tick(level, pos, state)
    }
}