package earth.terrarium.techarium.common.utils

import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.animation.AnimatableManager
import software.bernie.geckolib.animation.AnimationController
import software.bernie.geckolib.animation.AnimationState
import software.bernie.geckolib.animation.PlayState

fun <T : GeoAnimatable> AnimatableManager.ControllerRegistrar.register(
    animatable: T,
    name: String,
    time: Int = 0,
    handler: AnimationState<T>.() -> PlayState
) {
    this.add(AnimationController(animatable, name, time, handler))
}