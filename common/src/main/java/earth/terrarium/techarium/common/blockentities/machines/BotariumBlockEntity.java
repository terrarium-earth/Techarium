package earth.terrarium.techarium.common.blockentities.machines;

import earth.terrarium.botarium.common.energy.impl.InsertOnlyEnergyContainer;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.impl.InsertOnlyFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.techarium.common.blockentities.base.DeploymentManager;
import earth.terrarium.techarium.common.blockentities.base.RecipeMachineBlockEntity;
import earth.terrarium.techarium.common.menus.machines.BotariumMenu;
import earth.terrarium.techarium.common.recipes.machines.BotariumRecipe;
import earth.terrarium.techarium.common.registry.ModRecipeTypes;
import earth.terrarium.techarium.common.registry.ModSoundEvents;
import earth.terrarium.techarium.common.utils.ItemUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class BotariumBlockEntity extends RecipeMachineBlockEntity<BotariumRecipe> implements BotariumFluidBlock<WrappedBlockFluidContainer> {
    public static final RawAnimation DEPLOY = RawAnimation.begin().thenPlayAndHold("Deploy");
    public static final RawAnimation IDLE = RawAnimation.begin().thenPlayAndHold("Idle");
    public static final RawAnimation SPRINKLE = RawAnimation.begin().thenLoop("Sprinkle");

    private long lastFluid;
    private long fluidDifference;
    @Nullable
    private Block cropBlock;
    private WrappedBlockFluidContainer fluidContainer;
    private final DeploymentManager deploymentManager = new DeploymentManager(this, 25, 75, ModSoundEvents.BOTARIUM_DEPLOY.get());

    public BotariumBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state, 9);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, state -> {
            if (this.cookTimeTotal > 0) return state.setAndContinue(SPRINKLE);
            return deploymentManager.isDeployed() ? state.setAndContinue(IDLE) : state.setAndContinue(DEPLOY);
        }));
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return deploymentManager.isDeployed() ? new BotariumMenu(i, inventory, this) : null;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("CropBlock")) {
            cropBlock = BuiltInRegistries.BLOCK.get(new ResourceLocation(tag.getString("CropBlock")));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        if (cropBlock != null) {
            tag.putString("CropBlock", BuiltInRegistries.BLOCK.getKey(cropBlock).toString());
        }
    }

    @Override
    public boolean shouldSync() {
        return getEnergyStorage().getStoredEnergy() > 0 && !getFluidContainer().getFluids().get(0).isEmpty();
    }

    @Override
    public boolean shouldUpdate() {
        return shouldSync();
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
    public void firstTick(Level level, BlockPos pos, BlockState state) {
        super.firstTick(level, pos, state);
        deploymentManager.firstTick(level, pos);
    }

    @Override
    public void serverTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        super.serverTick(level, time, state, pos);
        deploymentManager.tickDeployment(level, pos, state);
    }

    @Override
    public void recipeTick(ServerLevel level, WrappedBlockEnergyContainer energyStorage) {
        if (recipe == null) return;
        if (fluidContainer == null) getFluidContainer();
        if (!canCraft(energyStorage)) {
            clearRecipe();
            return;
        }

        energyStorage.internalExtract(recipe.energy(), false);
        fluidContainer.internalExtract(recipe.fertilizer(), false);

        cookTime++;
        if (cookTime < cookTimeTotal) return;
        craft();
    }

    @Override
    public boolean canCraft(WrappedBlockEnergyContainer energyStorage) {
        if (recipe == null) return false;
        if (energyStorage.internalExtract(recipe.energy(), true) < recipe.energy()) return false;
        if (!recipe.seed().test(getItem(4))) return false;
        if (!recipe.soil().test(getItem(5))) return false;
        if (!ItemUtils.canAddItem(this, recipe.resultCrop(), 6, 7, 8)) return false;
        if (!ItemUtils.canAddItem(this, recipe.resultSeed(), 6, 7, 8)) return false;
        return fluidContainer.internalExtract(recipe.fertilizer(), true).getFluidAmount() >= recipe.fertilizer().getFluidAmount();
    }

    @Override
    public void craft() {
        if (recipe == null) return;

        getItem(4).shrink(1);
        ItemUtils.addItem(this, recipe.resultCrop(), 6, 7, 8);
        ItemUtils.addItem(this, recipe.resultSeed(), 6, 7, 8);

        updateSlots();

        cookTime = 0;
        if (fluidContainer.getFluids().get(0).isEmpty()) clearRecipe();
    }

    @Override
    public void update() {
        if (level().isClientSide()) return;
        level().getRecipeManager().getAllRecipesFor(ModRecipeTypes.BOTARIUM.get())
            .stream()
            .filter(r -> r.fertilizer().matches(getFluidContainer().getFluids().get(0)))
            .filter(r -> r.seed().test(getItem(4)))
            .filter(r -> r.soil().test(getItem(5)))
            .findFirst()
            .ifPresent(r -> {
                recipe = r;
                cookTimeTotal = r.cookingTime();
                cropBlock = r.cropBlock();
            });
        updateSlots();
    }

    @Override
    public void clearRecipe() {
        super.clearRecipe();
        cropBlock = null;
    }

    @Override
    public void clientTick(ClientLevel level, long time, BlockState state, BlockPos pos) {
        super.clientTick(level, time, state, pos);
        if (time % 2 == 0) return;
        fluidDifference = getFluidContainer().getFluids().get(0).getFluidAmount() - lastFluid;
        lastFluid = getFluidContainer().getFluids().get(0).getFluidAmount();
    }

    public long fluidDifference() {
        return fluidDifference;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{4, 5, 6, 7, 8, 9};
    }

    @Nullable
    public Block cropBlock() {
        return cropBlock;
    }
}
