package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.blockentity.selfdeploying.module.FluidModule;
import com.techarium.techarium.blockentity.selfdeploying.module.ItemModule;
import com.techarium.techarium.block.selfdeploying.SelfDeployingSlaveBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A Block Entity that will deploy itself in the world with an animation.<br>
 * See {@link SelfDeployingBlock} for the associated block.<br>
 * <br>
 * Child class should override {@link SelfDeployingBlockEntity#getMachineSlaveLocations()} to determine the location of the slave blocks. Default implementation is there is no slaves.
 * These locations should match the BlockRegion given by {@link SelfDeployingBlock#getDeployedSize()}.
 */
public abstract class SelfDeployingBlockEntity extends BlockEntity implements IAnimatable {

	private final Map<BlockPos, SelfDeployingSlaveBlock> slaves;
	private AnimationFactory factory = new AnimationFactory(this);

	public SelfDeployingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.slaves = this.getMachineSlaveLocations();
	}

	/**
	 * Deploy the block.
	 * Default implementation place the slaves blocks.
	 */
	public void deploy() {
		if (this.level != null) {
			for (Map.Entry<BlockPos, SelfDeployingSlaveBlock> entry : slaves.entrySet()) {
				this.level.setBlock(entry.getKey(), entry.getValue().defaultBlockState(), 3);
				BlockEntity blockEntity = level.getBlockEntity(entry.getKey());
				if (blockEntity instanceof SelfDeployingSlaveBlockEntity slaveBlockEntity) {
					slaveBlockEntity.setMasterPosition(this.worldPosition);
				}
			}
		}
	}

	/**
	 * Undeploy the block.
	 * Default implementation remove the slaves blocks alongside the master.
	 *
	 * @param removeSelf        determine if this block should remove itself
	 * @param restoreMultiBlock determine if the multiblock associated (if any) should be restored in the world.
	 * @param oldState          the state of the core block before it was removed
	 * @param initiator         the position of the block that initiated the removal of the self-deployed block
	 */
	public void undeploy(boolean removeSelf, boolean restoreMultiBlock, BlockState oldState, BlockPos initiator) {
		if (level != null) {
			this.level.removeBlockEntity(this.worldPosition);  // state changed but not yet the block entity so we do it now
			if (removeSelf) {
				// called from Block#onRemove, the state have already been changed. This is mainly used for when it is called from elsewhere.
				level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
			}
			// TODO @Ketheroth: 11/06/2022 drop content if there is an inventory
			for (BlockPos pos : slaves.keySet()) {
				if (level.getBlockState(pos).getBlock() instanceof SelfDeployingSlaveBlock) {
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
	}

	/**
	 * Determine the locations of the slave block of this self-deploying block.
	 * The locations are assumed to be real world position.
	 *
	 * @return the locations of the slaves.
	 */
	public Map<BlockPos, SelfDeployingSlaveBlock> getMachineSlaveLocations() {
		return new HashMap<>();
	}

	/**
	 * Called when the associated block is used.
	 */
	public abstract InteractionResult onUse(Player player, InteractionHand hand);

	@Override
	public void registerControllers(AnimationData animationData) {
		animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
	}

	protected abstract <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event);

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	/**
	 * A self-deploying block entity with inventory modules.
	 */
	// TODO @anyone: 11/07/2022 change it, it is a wip
	// TODO @Ketheroth: 17/06/2022 see if I can/should move this in the super-class
	public static abstract class WithModules extends SelfDeployingBlockEntity implements MenuProvider {

		private final ItemModule itemInput;
		private final ItemModule itemOutput;
		private final FluidModule fluidInput;

		public WithModules(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
			itemInput = this.createItemInput();
			itemOutput = this.createItemOutput();
			fluidInput = this.createFluidInput();
		}

		/**
		 * Default implementation : empty item module
		 * @return the item input of the machine.
		 */
		protected ItemModule createItemInput() {
			return ItemModule.EMPTY;
		}

		/**
		 * Default implementation : empty item module
		 * @return the item output of the machine.
		 */
		protected ItemModule createItemOutput() {
			return ItemModule.EMPTY;
		}

		/**
		 * Default implementation : empty fluid module
		 * @return the fluid input of the machine.
		 */
		protected FluidModule createFluidInput() {
			return FluidModule.EMPTY;
		}

		public ItemModule getItemInput() {
			return this.itemInput;
		}

		public ItemModule getItemOutput() {
			return this.itemOutput;
		}

		public FluidModule getFluidInput() {
			return this.fluidInput;
		}

		@Nullable
		@Override
		public Packet<ClientGamePacketListener> getUpdatePacket() {
			return ClientboundBlockEntityDataPacket.create(this);
		}

		@Override
		public CompoundTag getUpdateTag() {
			return this.saveWithoutMetadata();
		}

		@Override
		protected void saveAdditional(CompoundTag tag) {
			CompoundTag tagItemInput = new CompoundTag();
			this.itemInput.save(tagItemInput);
			tag.put("itemInput", tagItemInput);
			CompoundTag tagItemOutput = new CompoundTag();
			this.itemOutput.save(tagItemOutput);
			tag.put("itemOutput", tagItemOutput);
			CompoundTag tagFluidInput = new CompoundTag();
			this.fluidInput.save(tagFluidInput);
			tag.put("fluidInput", tagFluidInput);
		}

		@Override
		public void load(CompoundTag tag) {
			this.itemInput.load(tag.getCompound("itemInput"));
			this.itemOutput.load(tag.getCompound("itemOutput"));
			this.fluidInput.load(tag.getCompound("fluidInput"));
		}

	}

}
