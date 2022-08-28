package earth.terrarium.techarium.block.entity.singleblock;

import earth.terrarium.techarium.registry.TechariumBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class GravMagnetBlockEntity extends BlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    private Boolean isPull = false;

    public GravMagnetBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TechariumBlockEntities.GRAVMAGNET.get() ,blockPos, blockState);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    protected <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder()
                .addAnimation("pushing", true)
                .addAnimation("pulling", true));
        return PlayState.CONTINUE;
    }

    public Boolean isPull() {
        return isPull;
    }

    public void setPull(Boolean pull) {
        isPull = pull;
    }
}
