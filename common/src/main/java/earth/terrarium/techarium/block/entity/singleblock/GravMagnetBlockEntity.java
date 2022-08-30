package earth.terrarium.techarium.block.entity.singleblock;

import earth.terrarium.techarium.block.singleblock.GravMagnetBlock;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.util.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.resource.GeckoLibCache;

public class GravMagnetBlockEntity extends BlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    //3 for 3 blocks
    //4.9 for 5 blocks
    //7 for 8 blocks
    //9.5 for 12 blocks
    public float power = 8f;

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

    private  <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        //12
        //26 for 3 blocks
        //55 for 5 blocks
        //100 for 8 blocks
        //160 for 12 blocks
        GeckoLibCache.getInstance().parser.setValue("variable.distance", 26f/3f*power);

        String animation = isPull() ? "pulling" : "pushing";
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animation, true));

        return PlayState.CONTINUE;
    }

    public boolean isPull() {
        return getBlockState().getValue(GravMagnetBlock.POWERED);
    }

    public void tick(){
        pushPullEntities();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void pushPullEntities(){
        Direction dir = getBlockState().getValue(BlockStateProperties.FACING);
        if (level == null){
            return;
        }
        for (Entity entity : level.getEntitiesOfClass(Entity.class , new AABB(this.getBlockPos().offset(0, dir == Direction.UP || dir == Direction.DOWN ? -1 : 1, 0) , new BlockPos(MathUtils.vec3FromOffset(new Vec3(this.worldPosition.getX() , this.worldPosition.getY(), this.worldPosition.getZ()), dir, isPull() ? power : power-1)).relative(dir.getAxis() != Direction.Axis.Y ? dir.getClockWise().getAxis() : Direction.Axis.X, 1).relative(Direction.Axis.Z, dir.getAxis() != Direction.Axis.Y ? 0 :1).relative(Direction.Axis.Y, dir.getAxis() != Direction.Axis.Y ? 0 :1).relative(dir, dir == Direction.SOUTH || dir == Direction.EAST ? 1 : 0)))){
            Vec3 force = getForceWithDistance(entity, dir, isPull());
            entity.hurtMarked = true;
            entity.setDeltaMovement(force);
        }

    }

    private Vec3 getForceWithDistance(Entity entity, Direction dir, boolean isPull){
        double entityMass = Math.max(0.4, entity.getBbWidth() * entity.getBbHeight());
        if (dir == Direction.DOWN && isPull) {
            entityMass = 0.3;
        }
        float dist = Math.max(0.0001f,(float)(Math.abs(entity.position().get(dir.getAxis()) - (this.worldPosition.get(dir.getAxis())+0.5))));
        float distPower = ((float) ((power/ (dir == Direction.UP ? 1 : 5)) / (entityMass* 5*( dir == Direction.UP ? 3 : 1) * Math.pow(dist, 1))));


        int mul = isPull  ? -1 : 1;

        if(isPull && dist < 1){
            distPower = 0;
        }

        return entity.getDeltaMovement().add(dir.getStepX() * distPower * mul, dir.getStepY() * distPower * mul,
                dir.getStepZ() * distPower * mul);
    }
}
