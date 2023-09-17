package earth.terrarium.techarium.common.blockentities.base;

import earth.terrarium.techarium.common.registry.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class DeployableBlockEntity extends ContainerMachineBlockEntity {
    private int deploymentTicks = 0;

    public DeployableBlockEntity(BlockPos pos, BlockState state, int containerSize) {
        super(pos, state, containerSize);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("DeploymentTicks", deploymentTicks);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        deploymentTicks = tag.getInt("DeploymentTicks");
    }

    @Override
    public void firstTick(Level level, BlockPos pos, BlockState state) {
        super.firstTick(level, pos, state);
        if (level.isClientSide()) return;
        if (deploymentTicks < getDeploymentTime()) {
            level.playSound(null, pos, getDeploySound(), SoundSource.BLOCKS, 0.5f, 1.0f);
        }
    }

    @Override
    public void internalServerTick(ServerLevel level, long time, BlockState state, BlockPos pos) {
        super.internalServerTick(level, time, state, pos);
        if (deploymentTicks < getPartialDeploymentTime()) {
            deploymentTicks++;
        } else if (deploymentTicks < getDeploymentTime()) {
            level.setBlockAndUpdate(pos, state.setValue(DeployableBlock.DEPLOYED, DeployableBlock.DeployState.PARTIALLY_DEPLOYED));
            deploymentTicks++;
        } else {
            level.setBlockAndUpdate(pos, state.setValue(DeployableBlock.DEPLOYED, DeployableBlock.DeployState.DEPLOYED));
        }
    }

    public boolean isDeployed() {
        return getBlockState().getValue(DeployableBlock.DEPLOYED).isDeployed();
    }

    public SoundEvent getDeploySound() {
        return ModSoundEvents.BOTARIUM_DEPLOY.get();
    }

    public abstract int getPartialDeploymentTime();

    public abstract int getDeploymentTime();
}
