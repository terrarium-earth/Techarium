package earth.terrarium.techarium.block.entity.singleblock;

import earth.terrarium.techarium.block.singleblock.MagneticAcceleratorBlock;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.util.MagnetTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class MagneticAcceleratorBlockEntity extends BlockEntity implements IAnimatable{
    public MagneticAcceleratorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(TechariumBlockEntities.MAGNETIC_ACCELORATOR.get() ,blockPos, blockState);
    }

    private final AnimationFactory factory = new AnimationFactory(this);
    public float power = 3f;
    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.power = tag.getFloat("power");
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("power", power);
    }
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }
    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    private  <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {

        String animation = isPull() ? "pulling" : "pushing";
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animation, true));

        return PlayState.CONTINUE;
    }
    public boolean isPull() {
        return getBlockState().getValue(MagneticAcceleratorBlock.POWERED);
    }

    public void pushPullEntities(Entity entity){
        Direction dir = getBlockState().getValue(BlockStateProperties.FACING);
        if (level == null){
            return;
        }
        Vec3 force = getForceWithDistance(entity, dir, isPull());
        entity.hurtMarked = true;
        entity.setDeltaMovement(force);

    }
    private Vec3 getForceWithDistance(Entity entity, Direction dir, boolean isPull){
        double entityMass = Math.max(0.4, entity.getBbWidth() * entity.getBbHeight());

        var distPower = entityMass * power * (isPull ? -1 : 1);

        return entity.getDeltaMovement().add(dir.getStepX() * distPower, dir.getStepY() * distPower,
                dir.getStepZ() * distPower);
    }
}
