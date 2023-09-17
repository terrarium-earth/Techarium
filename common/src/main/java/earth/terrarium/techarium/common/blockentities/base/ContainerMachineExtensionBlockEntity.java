package earth.terrarium.techarium.common.blockentities.base;

import earth.terrarium.techarium.common.registry.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ContainerMachineExtensionBlockEntity extends BlockEntity {

    public ContainerMachineExtensionBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntityTypes.MACHINE_EXTENSION.get(), pos, blockState);
    }
}
