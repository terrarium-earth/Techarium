package com.techarium.techarium.block.inventory;

import com.techarium.techarium.blockentity.selfdeploying.BotariumBlockEntity;
import com.techarium.techarium.platform.CommonServices;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BotariumMenu extends AbstractContainerMenu {

	private final BlockPos pos;
	private final Player player;
	private final Inventory playerInventory;
	private final SimpleContainer input;
	private final SimpleContainer output;
	private final BotariumBlockEntity botarium;

	public BotariumMenu(int id, Inventory playerInventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.BOTARIUM.get(), id);
		this.pos = pos;
		this.player = player;
		this.playerInventory = playerInventory;
		this.botarium = (BotariumBlockEntity) this.player.level.getBlockEntity(this.pos);

		this.input = this.botarium.getItemInput();
		this.output = this.botarium.getItemOutput();

		this.addSlot(new Slot(input, 0, 49, 35));
		this.addSlot(new Slot(input, 1, 49, 67));

		this.addSlot(new OutputSlot(output, 0, 83, 81));
		this.addSlot(new OutputSlot(output, 1, 103, 81));
		this.addSlot(new OutputSlot(output, 2, 123, 81));
		this.addSlot(new OutputSlot(output, 3, 143, 81));

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				this.addSlot(new Slot(this.playerInventory, 9 + y * 9 + x, 7 + 18 * x, 103 + 18 * y));
			}
		}
		for (int x = 0; x < 9; x++) {
			this.addSlot(new Slot(this.playerInventory, x, 7 + 18 * x, 160));
		}
	}

	@Override
	public void removed(Player player) {
		this.input.setChanged();
		this.output.setChanged();
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			itemstack = slotStack.copy();
			if (0 <= index && index <= 1) {  // shift-click in input slot
				if (!this.moveItemStackTo(slotStack, 6, 42, true)) {
					return ItemStack.EMPTY;
				}
			} else if (2 <= index && index <= 5) {  // in output slot
				if (!this.moveItemStackTo(slotStack, 6, 42, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index < 42) {  // in player inventory
				if (!this.moveItemStackTo(slotStack, 0, 2, false)) {
					return ItemStack.EMPTY;
				}
			}
			if (slotStack.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (slotStack.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotStack);
		}
		return itemstack;
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ()) <= 16.0D;
	}

	public long getFluidAmount() {
		return this.botarium.getFluidInput().currentAmount();
	}

	private static class OutputSlot extends Slot {

		public OutputSlot(Container container, int index, int x, int y) {
			super(container, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}

	}

}
