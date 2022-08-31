package earth.terrarium.techarium.inventory;

import earth.terrarium.techarium.block.entity.selfdeploying.ExchangeStationBlockEntity;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ExchangeStationMenu extends MachineMenu {

	private final ExchangeStationBlockEntity exchangeStation;

	public ExchangeStationMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
		this(id, inventory, inventory.player, buffer.readBlockPos());
	}

	public ExchangeStationMenu(int id, Inventory inventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.EXCHANGE_STATION.get(), id, pos);
		this.exchangeStation = TechariumBlockEntities.EXCHANGE_STATION.get().getBlockEntity(player.level, this.pos);

		if (exchangeStation != null) {
			this.addSlot(new Slot(exchangeStation, 0, 99, 35));

			// TODO future:
			//  we're using FurnaceResultSlot because there is not a crafting container available
			//  when there is one, replace it with ResultSlot
			this.addSlot(new FurnaceResultSlot(player, exchangeStation, 0, 153, 35));
		}

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(inventory, 9 + y * 9 + x, 16 + 18 * x, 148 + 18 * y));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(inventory, x, 16 + 18 * x, 206));
		}
	}

	@Override
	public void removed(Player player) {
		this.exchangeStation.setChanged();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

}
