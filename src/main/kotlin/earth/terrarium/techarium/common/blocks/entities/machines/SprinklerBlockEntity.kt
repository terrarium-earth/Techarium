package earth.terrarium.techarium.common.blocks.entities.machines

import com.teamresourceful.resourcefullibkt.common.contains
import earth.terrarium.techarium.common.blocks.entities.DeployableBlockEntity
import earth.terrarium.techarium.common.capabilities.blocks.ComponentFluidHandler
import earth.terrarium.techarium.common.registries.ModAttachments
import earth.terrarium.techarium.common.registries.ModBlockEntityTypes
import earth.terrarium.techarium.common.registries.ModBlocks
import earth.terrarium.techarium.common.registries.ModComponents
import earth.terrarium.techarium.common.utils.*
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BonemealableBlock
import net.minecraft.world.level.block.LevelEvent
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED
import net.neoforged.neoforge.fluids.FluidType
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.PlayState
import software.bernie.geckolib.animation.RawAnimation


private const val TICKS_PER_OPERATION = 20 * 2
private const val DRAIN_AMOUNT = 10
private val DEPLOY = RawAnimation.begin().thenPlay("deploy").thenLoop("idle")
private val IDLE = RawAnimation.begin().thenLoop("idle")
private val WORK = RawAnimation.begin().thenPlay("work")

class SprinklerBlockEntity(
    pos: BlockPos,
    state: BlockState
) : DeployableBlockEntity(ModBlockEntityTypes.sprinkler, pos, state) {

    private var workTicks = 0
    private val isWorking: Boolean
        get() = (this.uniqueId + this.ticks) % TICKS_PER_OPERATION == 0L && !this.blockState.getValue(POWERED)

    val tank = ComponentFluidHandler.create(ComponentSlot.INPUT, ModAttachments.singleFluidTank, this) {
        it.`is`(FluidTags.WATER)
    }

    override val initialComponents: DataComponentMap = buildComponentMap {
        set(ModComponents.sprinklerRadius, 4)
        set(ModComponents.tankCapacity, ComponentSlot.INPUT.asComponent(FluidType.BUCKET_VOLUME * 16))
    }

    override fun triggerEvent(id: Int, type: Int): Boolean {
        return when (id) {
            1 -> {
                if (level?.isClientSide == false) return true

                val radius = type / 8.0
                for (archimedeanSpiralPoint in this.blockPos.getArchimedeanSpiralPoints(radius)) {
                    level?.addAlwaysVisibleParticle(
                        ParticleTypes.SPLASH,
                        false,
                        archimedeanSpiralPoint.x.toDouble(),
                        archimedeanSpiralPoint.y.toDouble(),
                        archimedeanSpiralPoint.z.toDouble(),
                        0.0,
                        0.0,
                        0.0,
                    )
                }
                true
            }
            else -> false
        }
    }

    override fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState) {
        if (!isWorking) return
        workTicks++

        val radius = this[ModComponents.sprinklerRadius] ?: return
        if (!tank.tryDrainInternal(DRAIN_AMOUNT)) return

        val center = this.blockPos
        val position = MutableBlockPos()

        sendEvent(1, radius)

        for (x in -radius..radius) {
            for (z in -radius..radius) {
                var target = position.setWithOffset(center, x, -1, z)
                val farmland = level.getBlockState(target)
                if (farmland.block.equals(Blocks.FARMLAND) || farmland.block.equals(ModBlocks.stableFarmland)) {
                    level.setBlockAndUpdate(target, ModBlocks.stableFarmland.defaultBlockState())
                }
                if (workTicks % 5 != 0) continue
                if (level.random.nextInt(10) != 0) continue
                target = position.setWithOffset(center, x, 0, z)
                val crop = level.getBlockState(target)
                if (crop !in BlockTags.CROPS) return
                val block = crop.block as? BonemealableBlock ?: continue
                if (!block.isValidBonemealTarget(level, target, crop)) return
                block.performBonemeal(level, level.random, target, crop)
                level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, target, 15)
            }
        }
    }

    override fun registerControllers(controllers: AnimatableManager.ControllerRegistrar) {
        controllers.register(this, "sprinkler", 0) {
            when {
                !isDeployed -> setAndContinue(DEPLOY)
                isWorking -> setAndContinue(WORK)
                this.controller.hasAnimationFinished() -> setAndContinue(IDLE)
                else -> PlayState.CONTINUE
            }
        }
    }

}