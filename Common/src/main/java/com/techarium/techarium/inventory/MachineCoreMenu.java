package com.techarium.techarium.inventory;

import com.techarium.techarium.block.entity.multiblock.MachineCoreBlockEntity;
import com.techarium.techarium.multiblock.MultiblockStructure;
import com.techarium.techarium.registry.RegistryHelper;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class MachineCoreMenu extends AbstractContainerMenu {

	private final BlockPos pos;
	private final MachineCoreBlockEntity machineCore;

	public MachineCoreMenu(int id, Inventory playerInventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.MACHINE_CORE.get(), id);
		this.pos = pos;
		this.machineCore = TechariumBlockEntities.MACHINE_CORE.get().getBlockEntity(player.level, this.pos);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ()) <= 64.0D;
	}

	public MachineCoreBlockEntity getMachineCore() {
		return machineCore;
	}

	@Override
	public boolean clickMenuButton(Player player, int buttonId) {
		MultiblockStructure multiblockStructure = this.getMachineCore().getLevel().registryAccess().registry(RegistryHelper.getMultiblockRegistryKey()).get().byId(buttonId);
		this.machineCore.setMultiblock(multiblockStructure);
		return super.clickMenuButton(player, buttonId);
	}
}
