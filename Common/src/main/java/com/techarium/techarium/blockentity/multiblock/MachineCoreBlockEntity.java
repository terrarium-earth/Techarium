package com.techarium.techarium.blockentity.multiblock;

import com.techarium.techarium.block.multiblock.MachineCoreBlock;
import com.techarium.techarium.block.multiblock.MultiblockState;
import com.techarium.techarium.inventory.MachineCoreMenu;
import com.techarium.techarium.multiblock.MultiBlockStructure;
import com.techarium.techarium.platform.CommonServices;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A BlockEntity for the {@link MachineCoreBlock}.
 */
public class MachineCoreBlockEntity extends BlockEntity implements MenuProvider {
	// We don't save the selected multiblock
	//  1- The level isn't set when the block entity is loaded
	//  2- I (Ketheroth) don't want to cache the resource location until I can transform it in the multiblock structure.

	// TODO @anyone: 12/06/2022 change this to allow displaying for just a few seconds (see TODO.md)
	/**
	 * List of multiblock cores that are obstructed and can't deploy. This is used to render the obstruction overlays.
	 */
	public static List<BlockPos> CORE_WITH_OBSTRUCTION = new ArrayList<>();

	private MultiBlockStructure multiblock;

	public MachineCoreBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.MACHINE_CORE.get(), pos, state);
		this.multiblock = null;
	}




	// TODO: 11/07/2022 datapack reload
	// TODO: 11/07/2022 machine offset from the core









	public InteractionResult onActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
		// |multiblock selected | multiblock valid | texture  |     rclick           | shift-rclick|
		// |:------------------:|:----------------:|:--------:|:--------------------:|:-----------:|
		// |         yes        |        yes       |   green  | transform multiblock |  open gui   |
		// |         yes        |        no        |   red    | nothing              |  open gui   |
		// |         no         |      yes & no    |   blue   | open gui             |  open gui   |
		if (this.multiblock == null || player.isShiftKeyDown()) {
			CommonServices.PLATFORM.openGui(((ServerPlayer) player), this, buf -> buf.writeBlockPos(this.worldPosition));
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
			if (this.multiblock != null) {
				if (this.multiblock.isValidStructure(pos, state.getValue(BlockStateProperties.HORIZONTAL_FACING), level)) {
					if (this.multiblock.canConvert(level, state, pos)) {
						// TODO @Ashley: multiblock can convert now, don't display the overlay anymore
						level.setBlock(pos, state.setValue(MachineCoreBlock.MULTIBLOCK, MultiblockState.VALID), 3);
					} else {
						// TODO @Ashley: multiblock can't convert, display an overlay of the obstructing blocks. You can have the list of the obstructing blocks via MachineCoreBlockEntity#getObstructingBlocks().
					}
				} else {
					level.setBlock(pos, state.setValue(MachineCoreBlock.MULTIBLOCK, MultiblockState.INVALID), 3);
				}
			} else {
				// is it useful ? are we in another state than 0 if we're in this if ?
				level.setBlock(pos, state.setValue(MachineCoreBlock.MULTIBLOCK, MultiblockState.NONE), 3);
			}
		}
	}

	public List<BlockPos> getObstructingBlocks(Level level, BlockPos pos) {
		if (this.multiblock == null) {
			return List.of();
		}
		return this.multiblock.getObstructingBlocks(level, pos);
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

	public void setMultiblock(ResourceLocation multiblockId) {
		this.multiblock = CommonServices.REGISTRY.getMultiBlockStructure(this.level, multiblockId);
	}

	public Optional<ResourceLocation> selectedMultiblock() {
		return CommonServices.REGISTRY.getMultiBlockKey(this.level, this.multiblock);
	}

}
