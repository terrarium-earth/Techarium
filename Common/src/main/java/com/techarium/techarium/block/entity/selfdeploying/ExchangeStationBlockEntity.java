package com.techarium.techarium.block.entity.selfdeploying;

import com.techarium.techarium.block.selfdeploying.SelfDeployingComponentBlock;
import com.techarium.techarium.inventory.ExchangeStationMenu;
import com.techarium.techarium.platform.CommonServices;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumBlocks;
import com.techarium.techarium.util.PlatformHelper;
import com.techarium.techarium.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.Map;

public class ExchangeStationBlockEntity extends SelfDeployingMultiBlockBlockEntity implements MenuProvider {

	public ExchangeStationBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.EXCHANGE_STATION.get(), pos, state);
	}

	@Override
	public InteractionResult onUse(Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer serverPlayer) {
			PlatformHelper.openMenu(serverPlayer, this);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder()
				.addAnimation("exchange_station.deploy", false)
				.addAnimation("exchange_station.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public Map<BlockPos, SelfDeployingComponentBlock> getMachineComponentLocations() {
		return Map.of(this.worldPosition.above(), TechariumBlocks.SELF_DEPLOYING_COMPONENT.get());
	}

	@Override
	public Component getDisplayName() {
		return Utils.translatableComponent("container.techarium.exchange_station");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new ExchangeStationMenu(id, inventory, player, this.worldPosition);
	}

	@Override
	public int getContainerSize() {
		return 2;
	}
}
