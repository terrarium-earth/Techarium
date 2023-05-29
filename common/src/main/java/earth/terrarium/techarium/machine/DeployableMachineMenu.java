package earth.terrarium.techarium.machine;

import earth.terrarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DeployableMachineMenu extends AbstractContainerMenu {

	private final BlockPos pos;
	private DeployableMachineBlockEntity machine;
	public DeployableMachineMenu(int id, Inventory playerInventory, FriendlyByteBuf buf) {
		super(TechariumMenuTypes.DEPLOYABLE_MACHINE.get(), id);
		this.pos = buf.readBlockPos();
		if (playerInventory.player.level.getBlockEntity(this.pos) instanceof DeployableMachineBlockEntity deployableMachine) {
			this.machine = deployableMachine;
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInventory, 9 + y * 9 + x, 7 + 18 * x, 103 + 18 * y));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(playerInventory, x, 7 + 18 * x, 160));
		}
	}

	public DeployableMachineMenu(int id, Inventory playerInventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.DEPLOYABLE_MACHINE.get(), id);
		this.pos = pos;
		if (player.level.getBlockEntity(this.pos) instanceof DeployableMachineBlockEntity deployableMachine) {
			this.machine = deployableMachine;
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ()) <= 64.0D;
	}

	public DeployableMachineBlockEntity getMachine() {
		return machine;
	}
}
