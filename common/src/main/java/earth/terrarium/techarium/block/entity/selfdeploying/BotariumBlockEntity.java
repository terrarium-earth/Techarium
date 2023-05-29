package earth.terrarium.techarium.block.entity.selfdeploying;

import earth.terrarium.botarium.api.energy.EnergyBlock;
import earth.terrarium.botarium.api.energy.SimpleUpdatingEnergyContainer;
import earth.terrarium.botarium.api.energy.StatefulEnergyContainer;
import earth.terrarium.techarium.block.selfdeploying.SelfDeployingComponentBlock;
import earth.terrarium.techarium.inventory.BotariumMenu;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.registry.TechariumBlocks;
import earth.terrarium.techarium.util.PlatformHelper;
import earth.terrarium.techarium.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.Map;

public class BotariumBlockEntity extends SelfDeployingBlockEntity.WithContainer implements EnergyBlock {

    private SimpleUpdatingEnergyContainer energyContainer;

    public BotariumBlockEntity(BlockPos pos, BlockState state) {
        super(TechariumBlockEntities.BOTARIUM.get(), pos, state);
    }

    @Override
    public InteractionResult onUse(Player player, InteractionHand hand) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (!this.handleBucketUse(serverPlayer)) {
                PlatformHelper.openMenu(serverPlayer, this);
            }
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    private boolean handleBucketUse(ServerPlayer player) {
        Item item = player.getMainHandItem().getItem();
        if (!(item instanceof BucketItem) || item instanceof MobBucketItem) {
            // TODO @anyone: 18/06/2022 change this to allow other portable tank (like mekanism tank)
            return false;
        }
        SimpleFluidContainer fluidInput = this.getFluidInput();
        if (item == Items.BUCKET) {
            if (fluidInput.isEmpty()) {
                return false;
            }
            if (!fluidInput.canRetrieve(SimpleFluidContainer.BUCKET_CAPACITY)) {
                return false;
            }

            fluidInput.retrieve(SimpleFluidContainer.BUCKET_CAPACITY);
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemUtils.createFilledResult(player.getMainHandItem(), player, new ItemStack(fluidInput.getFluid().getBucket())));
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
        } else {
            Fluid fluid = PlatformHelper.determineFluidFromItem(player.getMainHandItem());
            if (!fluid.isSame(Fluids.EMPTY)) {
                if (fluidInput.isEmpty() || fluidInput.getFluid().isSame(fluid)) {
                    boolean canAdd = fluidInput.canAdd(SimpleFluidContainer.BUCKET_CAPACITY);
                    if (!canAdd) {
                        return false;
                    }
                    fluidInput.add(fluid, SimpleFluidContainer.BUCKET_CAPACITY);
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemUtils.createFilledResult(player.getMainHandItem(), player, new ItemStack(Items.BUCKET)));
                    this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder()
                .addAnimation("botarium.deploy", false)
                .addAnimation("botarium.idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public Map<BlockPos, SelfDeployingComponentBlock> getMachineComponentLocations() {
        return Map.of(this.worldPosition.above(), TechariumBlocks.SELF_DEPLOYING_COMPONENT.get());
    }

    @Override
    public Component getDisplayName() {
        return Utils.translatableComponent("container.techarium.botarium");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new BotariumMenu(id, inventory, player, this.worldPosition);
    }

    @Override
    public int getContainerSize() {
        return 6;
    }

    @Override
    protected SimpleFluidContainer createFluidInput() {
        return new SimpleFluidContainer(SimpleFluidContainer.BUCKET_CAPACITY * 12);
    }

    @Override
    public SimpleUpdatingEnergyContainer getEnergyStorage() {
        if (energyContainer == null) {
            this.energyContainer = new SimpleUpdatingEnergyContainer(this, 1000000);
        }
        return energyContainer;
    }

    @Override
    public void update() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
}
