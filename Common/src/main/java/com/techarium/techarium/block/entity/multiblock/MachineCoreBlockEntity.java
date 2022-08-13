package com.techarium.techarium.block.entity.multiblock;

import com.techarium.techarium.block.multiblock.MachineCoreBlock;
import com.techarium.techarium.block.multiblock.MultiblockState;
import com.techarium.techarium.inventory.ExtraDataMenuProvider;
import com.techarium.techarium.inventory.MachineCoreMenu;
import com.techarium.techarium.multiblock.MultiblockStructure;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.util.PlatformHelper;
import com.techarium.techarium.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * A BlockEntity for the {@link MachineCoreBlock}.
 */
public class MachineCoreBlockEntity extends BlockEntity implements ExtraDataMenuProvider {
	// We don't save the selected multiblock
	//  1- The level isn't set when the block entity is loaded
	//  2- I (Ketheroth) don't want to cache the resource location until I can transform it in the multiblock structure.

	private MultiblockStructure multiblock;

	public MachineCoreBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.MACHINE_CORE.get(), pos, state);
	}

	public InteractionResult onActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
		if (this.multiblock == null || player.isShiftKeyDown()) {
			PlatformHelper.openMenu(((ServerPlayer) player), this);
			return InteractionResult.SUCCESS;
		}

		if (this.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
			if (this.multiblock.canConvert(level, state, pos)) {
				this.multiblock.convert(level, state, pos);
				return InteractionResult.SUCCESS;
			}
			// TODO @Ashley: multiblock can't convert, display an overlay of the obstructing blocks. You can have the list of the obstructing blocks via MachineCoreBlockEntity#getObstructingBlocks().
		}
		return InteractionResult.PASS;
	}

	public void tick(Level level, BlockPos pos, BlockState state) {
		if (level.getGameTime() % 5 == 0) {
			if (multiblock != null) {
				if (multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
					if (multiblock.canConvert(level, state, pos)) {
						// TODO @Ashley: multiblock can convert now, don't display the overlay anymore
						level.setBlock(pos, state.setValue(MachineCoreBlock.MULTIBLOCK_STATE, MultiblockState.VALID), 3);
					} else {
						// TODO @Ashley: multiblock can't convert, display an overlay of the obstructing blocks. You can have the list of the obstructing blocks via MachineCoreBlockEntity#getObstructingBlocks().
					}
				} else {
					level.setBlock(pos, state.setValue(MachineCoreBlock.MULTIBLOCK_STATE, MultiblockState.INVALID), 3);
				}
			}
		}
	}

	public List<BlockPos> getObstructingBlocks(Level level, BlockPos pos) {
		return this.multiblock == null ? Collections.emptyList() : this.multiblock.getObstructingBlocks(level, pos);
	}

	@Override
	public Component getDisplayName() {
		return Utils.translatableComponent("container.techarium.machine_core");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return new MachineCoreMenu(id, inv, player, this.worldPosition);
	}

	public void setMultiblock(MultiblockStructure multiblock) {
		this.multiblock = multiblock;
	}

	public MultiblockStructure selectedMultiblock() {
		return multiblock;
	}

	@Override
	public void writeExtraData(ServerPlayer player, FriendlyByteBuf buffer) {
		buffer.writeBlockPos(this.worldPosition);
	}
}
