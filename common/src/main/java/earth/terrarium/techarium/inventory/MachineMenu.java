package earth.terrarium.techarium.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class MachineMenu extends AbstractContainerMenu {

    protected final BlockPos pos;

    protected MachineMenu(@Nullable MenuType<?> type, int id, BlockPos pos) {
        super(type, id);
        this.pos = pos;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ()) <= 64.0D;
    }

}
