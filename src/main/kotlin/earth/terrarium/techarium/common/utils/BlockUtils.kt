package earth.terrarium.techarium.common.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.capabilities.BlockCapability
import org.joml.Vector3f
import kotlin.math.absoluteValue

fun Direction.toRotation(): Rotation =
    when (this) {
        Direction.SOUTH -> Rotation.CLOCKWISE_180
        Direction.WEST -> Rotation.COUNTERCLOCKWISE_90
        Direction.EAST -> Rotation.CLOCKWISE_90
        else -> Rotation.NONE
    }

fun <T : BlockEntity> BlockEntityType(factory: BlockEntityType.BlockEntitySupplier<T>, vararg blocks: Block): BlockEntityType<T> =
    BlockEntityType(factory, blocks.toSet(), null)

/**
 * A uniqueish id for a blockentites, will be used for offsetting certain things.
 */
val BlockEntity.uniqueId: Long
    get() = this.blockPos.asLong().absoluteValue

fun BlockPos.getArchimedeanSpiralPoints(radius: Double): Sequence<Vector3f> {
    val blockPos = this.bottomCenter
    return sequence {
        val b = 10 / 2 / Mth.PI
        var i = 0.0f
        while (i < Mth.TWO_PI * radius) {
            val x = b * i * Mth.cos(i * 57.2958f)
            val z = b * i * Mth.sin(i * 57.2958f)
            yield(Vector3f(blockPos.x.toFloat() + x, blockPos.y.toFloat(), blockPos.z.toFloat() + z))
            i += 0.01f
        }
    }
}

fun BlockEntity.sendEvent(id: Int, type: Int) =
    this.level?.blockEvent(this.blockPos, this.blockState.block, id, type)