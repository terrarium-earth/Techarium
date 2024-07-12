package earth.terrarium.techarium.common.utils

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Rotation

fun Direction.toRotation(): Rotation =
    when (this) {
        Direction.SOUTH -> Rotation.CLOCKWISE_180
        Direction.WEST -> Rotation.COUNTERCLOCKWISE_90
        Direction.EAST -> Rotation.CLOCKWISE_90
        else -> Rotation.NONE
    }