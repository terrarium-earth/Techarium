package earth.terrarium.techarium.block.entity.selfdeploying;

import earth.terrarium.botarium.api.BlockEnergyContainer;
import earth.terrarium.botarium.api.EnergyBlock;
import earth.terrarium.botarium.api.EnergyContainer;
import earth.terrarium.techarium.block.selfdeploying.SelfDeployingComponentBlock;
import earth.terrarium.techarium.block.selfdeploying.SelfDeployingBlock;
import earth.terrarium.techarium.inventory.ExtraDataMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Map;

/**
 * A Block Entity that will deploy itself in the world with an animation.<br>
 * See {@link SelfDeployingBlock} for the associated block.<br>
 */
public abstract class SelfDeployingBlockEntity extends BlockEntity implements IAnimatable {

	private final Map<BlockPos, SelfDeployingComponentBlock> components;
	private final AnimationFactory factory = new AnimationFactory(this);
	private EnergyContainer energyContainer;

	public SelfDeployingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.components = this.getMachineComponentLocations();
	}

	/**
	 * Deploy the block.
	 * Default implementation place the component blocks.
	 */
	public void deploy() {
		if (this.level != null) {
			for (Map.Entry<BlockPos, SelfDeployingComponentBlock> entry : components.entrySet()) {
				this.level.setBlock(entry.getKey(), entry.getValue().defaultBlockState(), 3);
				BlockEntity blockEntity = level.getBlockEntity(entry.getKey());
				if (blockEntity instanceof SelfDeployingComponentBlockEntity componentBlockEntity) {
					componentBlockEntity.setControllerPosition(this.worldPosition);
				}
			}
		}
	}

	/**
	 * Undeploy the block.
	 * Default implementation remove the component blocks alongside the controller.
	 *
	 * @param removeSelf        determine if this block should remove itself
	 * @param restoreMultiblock determine if the multiblock associated (if any) should be restored in the world.
	 * @param oldState          the state of the core block before it was removed
	 * @param initiator         the position of the block that initiated the removal of the self-deployed block
	 */
	public void undeploy(boolean removeSelf, boolean restoreMultiblock, BlockState oldState, BlockPos initiator) {
		if (level != null) {
			this.level.removeBlockEntity(this.worldPosition);  // state changed but not yet the block entity so we do it now
			if (removeSelf) {
				// called from Block#onRemove, the state have already been changed. This is mainly used for when it is called from elsewhere.
				level.setBlock(this.worldPosition, Blocks.AIR.defaultBlockState(), 3);
			}
			// TODO @Ketheroth: 11/06/2022 drop content if there is an inventory
			for (BlockPos pos : components.keySet()) {
				if (level.getBlockState(pos).getBlock() instanceof SelfDeployingComponentBlock) {
					level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
				}
			}
		}
	}

	/**
	 * Determine the locations of the component block of this self-deploying block.
	 * The locations are assumed to be real world position.
	 * <br>
	 * These locations should match the BlockRegion given by {@link SelfDeployingBlock#getDeployedSize()}.
	 *
	 * @return the locations of the components.
	 */
	public abstract Map<BlockPos, SelfDeployingComponentBlock> getMachineComponentLocations();

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
	 * A self-deploying block entity with an inventory.
	 */
	// TODO @anyone: 11/07/2022 change it, it is a wip
	// TODO @Ketheroth: 17/06/2022 see if I can/should move this in the super-class
	public static abstract class WithContainer extends SelfDeployingBlockEntity implements ExtraDataMenuProvider, Container {
		private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		private SimpleFluidContainer fluidInput;

		public WithContainer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
			fluidInput = this.createFluidInput();
		}

		/**
		 * Default implementation : empty fluid module
		 *
		 * @return the fluid input of the machine.
		 */
		protected SimpleFluidContainer createFluidInput() {
			return SimpleFluidContainer.EMPTY;
		}

		public SimpleFluidContainer getFluidInput() {
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
		protected void saveAdditional(@NotNull CompoundTag tag) {
			super.saveAdditional(tag);
			ContainerHelper.saveAllItems(tag, this.items);
			tag.put("FluidInput", this.fluidInput.save());
		}

		@Override
		public void load(@NotNull CompoundTag tag) {
			super.load(tag);
			this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
			ContainerHelper.loadAllItems(tag, this.items);
			this.fluidInput = new SimpleFluidContainer(tag.getCompound("FluidInput"));
		}

		@Override
		public void writeExtraData(ServerPlayer player, FriendlyByteBuf buffer) {
			buffer.writeBlockPos(this.worldPosition);
		}

		@Override
		public boolean isEmpty() {
			return this.items.stream().allMatch(ItemStack::isEmpty);
		}

		@Override
		public ItemStack getItem(int slot) {
			return items.get(slot);
		}

		@Override
		public ItemStack removeItem(int slot, int amount) {
			ItemStack stack = ContainerHelper.removeItem(this.items, slot, amount);
			if (!stack.isEmpty()) this.setChanged();

			return stack;
		}

		@Override
		public ItemStack removeItemNoUpdate(int slot) {
			return ContainerHelper.takeItem(this.items, slot);
		}

		@Override
		public void setItem(int slot, ItemStack stack) {
			items.set(slot, stack);
		}

		@Override
		public boolean stillValid(Player player) {
			return player.distanceToSqr(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()) <= 64.0D;
		}

		@Override
		public void clearContent() {
			items.clear();
		}
	}
}
