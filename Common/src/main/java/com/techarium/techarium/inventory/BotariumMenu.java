package com.techarium.techarium.inventory;

import com.techarium.techarium.block.entity.selfdeploying.BotariumBlockEntity;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

public class BotariumMenu extends MachineMenu {
	private final BotariumBlockEntity botarium;

	public BotariumMenu(int id, Inventory playerInventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.BOTARIUM.get(), id, pos);
		this.botarium = TechariumBlockEntities.BOTARIUM.get().getBlockEntity(player.level, this.pos);

		if (botarium != null) {
			this.addSlot(new Slot(botarium, 0, 49, 35));
			this.addSlot(new Slot(botarium, 1, 49, 67));

			// TODO future:
			//  we're using FurnaceResultSlot because there is not a crafting container available
			//  when there is one, replace it with ResultSlot
			this.addSlot(new FurnaceResultSlot(player, botarium, 2, 83, 81));
			this.addSlot(new FurnaceResultSlot(player, botarium, 3, 103, 81));
			this.addSlot(new FurnaceResultSlot(player, botarium, 4, 123, 81));
			this.addSlot(new FurnaceResultSlot(player, botarium, 5, 143, 81));
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

	@Override
	public void removed(Player player) {
		this.botarium.setChanged();
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


	public long getFluidAmount() {
		return this.botarium.getFluidInput().getAmount();
	}

	public Fluid getFluid() {
		return this.botarium.getFluidInput().getFluid();
	}

	public BotariumBlockEntity getBotarium() {
		return botarium;
	}
}
