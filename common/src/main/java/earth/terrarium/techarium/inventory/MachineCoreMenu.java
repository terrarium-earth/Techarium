package earth.terrarium.techarium.inventory;

import earth.terrarium.techarium.block.entity.multiblock.MachineCoreBlockEntity;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import earth.terrarium.techarium.registry.RegistryHelper;
import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.registry.TechariumMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class MachineCoreMenu extends AbstractContainerMenu {

	private final BlockPos pos;
	private final MachineCoreBlockEntity machineCore;

	public MachineCoreMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
		this(id, inventory.player, buffer.readBlockPos());
	}

	public MachineCoreMenu(int id, Player player, BlockPos pos) {
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
