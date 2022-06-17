package com.techarium.techarium.block.inventory;

import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

// TODO @Ketheroth: 17/06/2022 implement that so I can move some methods/fields in a super-class
public class ExchangeStationMenu extends AbstractContainerMenu {

	private final BlockPos pos;
	private final Player player;
	private final Inventory playerInventory;
	private final SimpleContainer input;
	private final SimpleContainer output;

	public ExchangeStationMenu(int id, Inventory playerInventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.EXCHANGE_STATION.get(), id);
		this.pos = pos;
		this.player = player;
		this.playerInventory = playerInventory;
		this.input = new SimpleContainer(1);
		this.output = new SimpleContainer(1);

		this.addSlot(new Slot(input, 0, 99, 35));

		this.addSlot(new Slot(output, 0, 153, 35));

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(this.playerInventory, 9 + y * 9 + x, 16 + 18 * x, 148 + 18 * y));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(this.playerInventory, x, 16 + 18 * x, 206));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

}
