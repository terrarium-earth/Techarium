package earth.terrarium.techarium.common.blockentities.base;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DeploymentManager {
    private final ContainerMachineBlockEntity machine;
    private final int partialTime;
    private final int totalTime;
    private final SoundEvent deploySound;

    private int deploymentTicks = 0;

    public DeploymentManager(ContainerMachineBlockEntity machine, int partialTime, int totalTime, SoundEvent deploySound) {
        this.machine = machine;
        this.partialTime = partialTime;
        this.totalTime = totalTime;
        this.deploySound = deploySound;
    }

    public boolean isDeployed() {
        return deploymentTicks >= totalTime || machine.getBlockState().getValue(DeployableBlock.DEPLOYED).isDeployed();
    }

    public void firstTick(Level level, BlockPos pos) {
        if (!isDeployed()) {
            level.playSound(null, pos, deploySound, SoundSource.BLOCKS, 0.5f, 1.0f);
        } else {
            deploymentTicks = totalTime;
        }
    }

    public void tickDeployment(Level level, BlockPos pos, BlockState state) {
        if (deploymentTicks < partialTime) {
            deploymentTicks++;
        } else if (deploymentTicks < totalTime) {
            level.setBlockAndUpdate(pos, state.setValue(DeployableBlock.DEPLOYED, DeployableBlock.DeployState.PARTIALLY_DEPLOYED));
            deploymentTicks++;
        } else {
            level.setBlockAndUpdate(pos, state.setValue(DeployableBlock.DEPLOYED, DeployableBlock.DeployState.DEPLOYED));
        }
    }
}
