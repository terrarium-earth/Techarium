package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.block.selfdeploying.SelfDeployingSlaveBlock;
import com.techarium.techarium.block.selfdeploying.SelfDeployingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A Block Entity that will deploy itself in the world with an animation.
 * See {@link SelfDeployingBlock} for the associated block.
 */
public abstract class SelfDeployingBlockEntity extends BlockEntity implements IAnimatable {

	private final Map<BlockPos, SelfDeployingSlaveBlock> slaves;
	private AnimationFactory factory = new AnimationFactory(this);
//	protected boolean isOpening;

	public SelfDeployingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		this.slaves = this.getMachineSlaveLocations();
//		this.isOpening = true;
	}

	/**
	 * Deploy the block.
	 * Default implementation place the slaves blocks and set opening at true.
	 */
	public void deploy() {
		if (this.level != null) {
			for (Map.Entry<BlockPos, SelfDeployingSlaveBlock> entry : slaves.entrySet()) {
				this.level.setBlock(entry.getKey(), entry.getValue().defaultBlockState(), 3);
				BlockEntity blockEntity = level.getBlockEntity(entry.getKey());
				if (blockEntity instanceof SelfDeployingSlaveBlockEntity slaveBlockEntity) {
					System.out.println("set mpos to " + this.worldPosition);
					slaveBlockEntity.setMasterPosition(this.worldPosition);
				}
			}
		}
	}

	/**
	 * Undeploy the block.
	 * Default implementation remove the slaves blocks alongside the master.
	 */
	public void undeploy() {
		// TODO @Ketheroth: 04/06/2022 add a way to reform the structure after the self-deployed block is destroyed ??
		if (level != null) {
			level.destroyBlock(this.worldPosition, true);
			for (BlockPos pos : slaves.keySet()) {
				if (level.getBlockEntity(pos) instanceof SelfDeployingSlaveBlockEntity) {
					level.destroyBlock(pos, false);
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

}
