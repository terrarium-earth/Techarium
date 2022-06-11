package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.block.selfdeploying.SelfDeployingSlaveBlock;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.Map;

public class ExchangeStationBlockEntity extends SelfDeployingMultiBlockBlockEntity {

	public ExchangeStationBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.EXCHANGE_STATION.get(), pos, state);
	}

	@Override
	public InteractionResult onUse(Player player, InteractionHand hand) {
		return InteractionResult.SUCCESS;
	}

	@Override
	protected <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
		// TODO @Ketheroth: 06/06/2022 ask what is the use of isOpening in the old code base
		event.getController().setAnimation(new AnimationBuilder()
				.addAnimation("exchange_station.deploy", false)
				.addAnimation("exchange_station.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public Map<BlockPos, SelfDeployingSlaveBlock> getMachineSlaveLocations() {
		return Map.of(this.worldPosition.above(), TechariumBlocks.SELF_DEPLOYING_SLAVE.get());
	}

}
