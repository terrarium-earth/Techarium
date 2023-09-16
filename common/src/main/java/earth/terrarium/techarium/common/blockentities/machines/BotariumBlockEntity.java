package earth.terrarium.techarium.common.blockentities.machines;

import earth.terrarium.botarium.common.energy.impl.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.impl.InsertOnlyFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.techarium.common.blockentities.base.ContainerMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class BotariumBlockEntity extends ContainerMachineBlockEntity implements BotariumFluidBlock<WrappedBlockFluidContainer> {
    public static final RawAnimation DEPLOY = RawAnimation.begin().thenPlayAndHold("Deploy");
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlayAndHold("Idle");
    public static final RawAnimation SPRINKLE = RawAnimation.begin().thenPlayAndHold("Sprinkle");

    private WrappedBlockFluidContainer fluidContainer;

    public BotariumBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, 6);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, state -> {
            return state.setAndContinue(DEPLOY);
        }));
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public WrappedBlockEnergyContainer getEnergyStorage() {
        if (energyContainer != null) return energyContainer;
        return energyContainer = new WrappedBlockEnergyContainer(
            this,
            new InsertOnlyEnergyContainer(50_000) {
                @Override
                public long maxInsert() {
                    return 500;
                }
            });
    }

    @Override
    public WrappedBlockFluidContainer getFluidContainer() {
        if (fluidContainer != null) return fluidContainer;
        return fluidContainer = new WrappedBlockFluidContainer(
            this,
            new InsertOnlyFluidContainer(
                t -> FluidHooks.buckets(10.0f),
                1,
                (tank, holder) -> holder.is(FluidTags.WATER)));
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1, 2, 3, 4, 5};
    }
}
