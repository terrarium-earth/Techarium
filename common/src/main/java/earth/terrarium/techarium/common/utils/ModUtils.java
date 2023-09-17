package earth.terrarium.techarium.common.utils;

import earth.terrarium.techarium.common.blockentities.base.ContainerMachineBlockEntity;
import earth.terrarium.techarium.common.menus.base.BasicContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public final class ModUtils {

    public static Optional<ContainerMachineBlockEntity> getMachineFromMenuPacket(BlockPos pos, Player player, Level level) {
        if (!(player.containerMenu instanceof BasicContainerMenu<?>))
            return Optional.empty(); // ensure the sender has the menu open
        if (player.distanceToSqr(pos.getCenter()) > Mth.square(8))
            return Optional.empty(); // ensure the sender is close enough to the block
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ContainerMachineBlockEntity container)) return Optional.empty();
        return Optional.of(container);
    }
}
