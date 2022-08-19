package earth.terrarium.techarium.block.entity.selfdeploying;

import earth.terrarium.techarium.registry.TechariumBlockEntities;
import earth.terrarium.techarium.registry.TechariumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A component block used to fill the air in the world. It will proxy received calls to its controller.<br>
 * See {@link SelfDeployingBlockEntity}, the controller block entity.
 */
public class SelfDeployingComponentBlockEntity extends BlockEntity {

	private BlockPos controllerPosition;

	public SelfDeployingComponentBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.SELF_DEPLOYING_COMPONENT.get(), pos, state);
		this.controllerPosition = BlockPos.ZERO;
	}

	public void setControllerPosition(BlockPos position) {
		this.controllerPosition = position;
	}

	/**
	 * When the component block is used, proxy the call to the block at controller position.
	 *
	 * @return the result of the interaction with the controller block
	 */
	public InteractionResult onUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		BlockEntity blockEntity = level.getBlockEntity(this.controllerPosition);
		if (blockEntity instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			return selfDeployingBlockEntity.onUse(player, hand);
		}
		return InteractionResult.SUCCESS;
	}

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		if (level.getBlockEntity(this.controllerPosition) instanceof SelfDeployingBlockEntity selfDeployingBlockEntity) {
			// default : the machine is removed and if it was from a multiblock the multiblock is restored
			selfDeployingBlockEntity.undeploy(true, !(stack.is(TechariumItems.TECH_TOOL.get()) || player.isShiftKeyDown()), level.getBlockState(this.controllerPosition), pos);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		tag.put("controller", NbtUtils.writeBlockPos(this.controllerPosition));
	}

	@Override
	public void load(CompoundTag tag) {
		this.controllerPosition = NbtUtils.readBlockPos(tag.getCompound("controller"));
	}

}
