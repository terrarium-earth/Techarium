package com.techarium.techarium.inventory;

import com.techarium.techarium.blockentity.selfdeploying.ExchangeStationBlockEntity;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ExchangeStationMenu extends MachineMenu {

	private final SimpleContainer input;
	private final SimpleContainer output;
	private final ExchangeStationBlockEntity exchangeStation;

	public ExchangeStationMenu(int id, Inventory playerInventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.EXCHANGE_STATION.get(), id, pos);
		this.exchangeStation = TechariumBlockEntities.EXCHANGE_STATION.get().getBlockEntity(player.level, this.pos);

		this.input = this.exchangeStation.getItemInput();
		this.output = this.exchangeStation.getItemOutput();

		this.addSlot(new Slot(input, 0, 99, 35));

		// TODO future:
		// we're using FurnaceResultSlot because there is not a crafting container available
		// when there is one, replace it with ResultSlot
		this.addSlot(new FurnaceResultSlot(player, output, 0, 153, 35));

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(playerInventory, 9 + y * 9 + x, 16 + 18 * x, 148 + 18 * y));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(playerInventory, x, 16 + 18 * x, 206));
		}
	}

	@Override
	public void removed(Player player) {
		this.input.setChanged();
		this.output.setChanged();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

}
