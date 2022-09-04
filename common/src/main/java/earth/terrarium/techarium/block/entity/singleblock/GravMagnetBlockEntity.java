package earth.terrarium.techarium.block.entity.singleblock;

import earth.terrarium.techarium.block.singleblock.GravMagnetBlock;
import earth.terrarium.techarium.data.recipes.MagnetCompressRecipe;
import earth.terrarium.techarium.data.recipes.MagnetSplitRecipe;
import earth.terrarium.techarium.util.MagnetTarget;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.util.WorldContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
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

import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

public class GravMagnetBlockEntity extends BlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    //3 for 3 blocks
    //4.9 for 5 blocks
    //7 for 8 blocks
    //9.5 for 12 blocks
    public float power = 3f;

    private final WorldContainer container;

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
        this.container = new WorldContainer(new AABB(this.getBlockPos(), this.getBlockPos()), this.getLevel());
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    private AABB getOverlap(){
        var dir = getBlockState().getValue(BlockStateProperties.FACING);
        var maxRange = power + 12;

        var magnet = IntStream
                .iterate(0, i -> i < maxRange, i -> i + 1)
                .mapToObj(i -> getBlockPos().relative(dir, i)).map(pos -> Objects.requireNonNull(getLevel()).getBlockEntity(pos, TechariumBlockEntities.GRAVMAGNET.get()))
                .filter(gravMagnet -> gravMagnet.isPresent() && gravMagnet.get().getBlockState().getValue(BlockStateProperties.FACING) == dir.getOpposite()).findFirst();

        if (magnet.isEmpty() || magnet.get().isEmpty()){
            return new AABB(this.getBlockPos(), this.getBlockPos());
        }

        var magnetPositions = IntStream.iterate(0, i -> i < this.power + 1, i -> i +1).mapToObj(i -> this.getBlockPos().relative(dir, i)).toList();
        var otherMagnetPositions = IntStream.iterate(0, i -> i < magnet.get().get().power + 1, i -> i +1).mapToObj(i -> magnet.get().get().getBlockPos().relative(magnet.get().get().getBlockState().getValue(BlockStateProperties.FACING), i)).toList();

        var matchingPositions = magnetPositions.stream().filter(pos -> otherMagnetPositions.stream().anyMatch(pos2 -> pos2.equals(pos))).sorted().toList();

        if(matchingPositions.size() < 1){
            return new AABB(this.getBlockPos(), this.getBlockPos());
        }
        return new AABB(matchingPositions.get(0), matchingPositions.get(matchingPositions.size() - 1).offset(1, 1, 1));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private  <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        //26 for 3 blocks
        //55 for 5 blocks
        //100 for 8 blocks
        //160 for 12 blocks
        GeckoLibCache.getInstance().parser.setValue("variable.distance", (power-1)*15);

        String animation = isPull() ? "pulling" : "pushing";
        event.getController().setAnimation(new AnimationBuilder().addAnimation(animation, true));

        return PlayState.CONTINUE;
    }

    public boolean isPull() {
        return getBlockState().getValue(GravMagnetBlock.POWERED);
    }

    public void tick(){
        handleItems();
        pushPullEntities();
    }

    private void craft(Recipe<? extends SimpleContainer> recipe, ItemEntity target){
        if (!(level instanceof ServerLevel)){
            return;
        }
        ((ServerLevel) level).sendParticles(ParticleTypes.SONIC_BOOM, target.getX(), target.getY(), target.getZ(), 0, 0, 0, 0, 0);
        container.removeItem(target.getItem(), 1);
        if (!target.isRemoved()){
            ((MagnetTarget) target).Techarium$setProgress(0);
        }
        if (recipe instanceof MagnetSplitRecipe splitRecipe){
            for (ItemStack itemStack : splitRecipe.result(container)) {
                container.addItem(itemStack);
            }
        } else if (recipe instanceof MagnetCompressRecipe compressRecipe){
            for (ItemStack itemStack : compressRecipe.result(container)) {
                container.addItem(itemStack);
            }
        }
    }

    private void handleItems(){
        if(container.getLevel() == null){
            container.setLevel(level);
        }
        container.setContainerSpace(getOverlap());
        container.updateContainerAll();
        var entities = container.getEntities();
        var target = entities.stream().filter(entity -> ((MagnetTarget) entity).Techarium$isTarget()).findFirst();
        if (target.isPresent()){
            var recipe = hasRecipe(target.get().getItem());
            if (recipe.isPresent()) {
                if(isPull()){
                    var confirmedRecipe = ((MagnetSplitRecipe) recipe.get());
                    if (((MagnetTarget) target.get()).Techarium$getProgress() > confirmedRecipe.getTime()){
                        craft(confirmedRecipe, target.get());
                    } else {
                        ((MagnetTarget) target.get()).Techarium$setProgress(((MagnetTarget) target.get()).Techarium$getProgress() + 0.5f);
                        ((ServerLevel) level).sendParticles(ParticleTypes.GLOW, target.get().getX(), target.get().getY(), target.get().getZ(), 0, 0, 0, 0, 0);
                    }
                } else {
                    var confirmedRecipe = ((MagnetCompressRecipe) recipe.get());
                    if (((MagnetTarget) target.get()).Techarium$getProgress() > confirmedRecipe.getTime()){
                        craft(confirmedRecipe, target.get());
                    } else {
                        ((MagnetTarget) target.get()).Techarium$setProgress(((MagnetTarget) target.get()).Techarium$getProgress() + 0.5f);
                    }
                }
            }
        }
        var itemStack = entities.stream().filter(entity -> hasRecipe(entity.getItem()).isPresent()).findFirst();
        itemStack.ifPresent(itemEntity -> ((MagnetTarget) itemEntity).Techarium$setIsTarget(true));
    }

    private Optional<? extends Recipe<? extends SimpleContainer>> hasRecipe(ItemStack itemStack) {
        return this.isPull() ? Objects.requireNonNull(level).getRecipeManager().getAllRecipesFor(MagnetSplitRecipe.Type.INSTANCE).stream().filter(r -> r.isValid(itemStack)).findFirst()
                : Objects.requireNonNull(level).getRecipeManager().getAllRecipesFor(MagnetCompressRecipe.Type.INSTANCE).stream().filter(r -> r.isValid(itemStack)).findFirst();
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
        for (Entity entity : level.getEntitiesOfClass(Entity.class , new AABB(this.getBlockPos().offset(0, dir == Direction.UP || dir == Direction.DOWN ? -1 : 1, 0) , new BlockPos(new Vec3(this.worldPosition.getX() , this.worldPosition.getY(), this.worldPosition.getZ()).relative(dir, isPull() ? power : power-1)).relative(dir.getAxis() != Direction.Axis.Y ? dir.getClockWise().getAxis() : Direction.Axis.X, 1).relative(Direction.Axis.Z, dir.getAxis() != Direction.Axis.Y ? 0 :1).relative(Direction.Axis.Y, dir.getAxis() != Direction.Axis.Y ? 0 :1).relative(dir, dir == Direction.SOUTH || dir == Direction.EAST ? 1 : 0)))){
            if (entity instanceof ItemEntity item && ((MagnetTarget) item).Techarium$isTarget()){
                continue;
            }
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

        if (!isPull && entity instanceof ServerPlayer player && dir == Direction.UP && !player.isShiftKeyDown()){
            var velocity =  entity.getDeltaMovement();
            return new Vec3(velocity.x + (dir.getStepX() * distPower * mul),velocity.y + (dir.getStepY() * distPower * mul),
                    velocity.z + (dir.getStepZ() * distPower * mul));
        }

        return entity.getDeltaMovement().add(dir.getStepX() * distPower * mul, dir.getStepY() * distPower * mul,
                dir.getStepZ() * distPower * mul);
    }
}
