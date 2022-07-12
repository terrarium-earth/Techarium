package com.techarium.techarium.inventory;

import com.techarium.techarium.blockentity.multiblock.MachineCoreBlockEntity;
import com.techarium.techarium.platform.CommonServices;
import com.techarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public class MachineCoreMenu extends AbstractContainerMenu {

	private final BlockPos pos;
	private final MachineCoreBlockEntity machineCore;

	public MachineCoreMenu(int id, Inventory playerInventory, Player player, BlockPos pos) {
		super(TechariumMenuTypes.MACHINE_CORE.get(), id);
		this.pos = pos;
		this.machineCore = (MachineCoreBlockEntity) player.level.getBlockEntity(this.pos);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return player.distanceToSqr(this.pos.getX(), this.pos.getY(), this.pos.getZ()) <= 64.0D;
	}

	public List<ResourceLocation> getAllMultiblockStructures() {
		return CommonServices.REGISTRY.getMultiBlockKeys(this.machineCore.getLevel());
	}

	public void setMultiBlock(ResourceLocation id) {
		this.machineCore.setMultiblock(id);
	}

	public Optional<ResourceLocation> selectedMultiblock() {
		return this.machineCore.selectedMultiblock();
	}

	@Override
	public boolean clickMenuButton(Player player, int buttonId) {
		ResourceLocation multiblockId = this.getAllMultiblockStructures().get(buttonId);
		this.setMultiBlock(multiblockId);
		return super.clickMenuButton(player, buttonId);
	}

}
