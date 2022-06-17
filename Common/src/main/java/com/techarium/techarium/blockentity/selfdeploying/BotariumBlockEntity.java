package com.techarium.techarium.blockentity.selfdeploying;

import com.techarium.techarium.block.inventory.BotariumMenu;
import com.techarium.techarium.block.selfdeploying.SelfDeployingSlaveBlock;
import com.techarium.techarium.blockentity.selfdeploying.module.FluidModule;
import com.techarium.techarium.blockentity.selfdeploying.module.InventoryModule;
import com.techarium.techarium.blockentity.selfdeploying.module.ItemModule;
import com.techarium.techarium.blockentity.selfdeploying.module.ModuleType;
import com.techarium.techarium.platform.CommonServices;
import com.techarium.techarium.registry.TechariumBlockEntities;
import com.techarium.techarium.registry.TechariumBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.HashMap;
import java.util.Map;

public class BotariumBlockEntity extends SelfDeployingBlockEntity.WithModules {

	public BotariumBlockEntity(BlockPos pos, BlockState state) {
		super(TechariumBlockEntities.BOTARIUM.get(), pos, state);
	}

	@Override
	public InteractionResult onUse(Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer serverPlayer) {
			if (!this.handleBucketUse(serverPlayer)) {
				CommonServices.PLATFORM.openGui(serverPlayer, this, buf -> buf.writeBlockPos(this.worldPosition));
			}
		}
		return InteractionResult.sidedSuccess(this.level.isClientSide);
	}

	private boolean handleBucketUse(ServerPlayer player) {
		Item item = player.getMainHandItem().getItem();
		if (item == Items.WATER_BUCKET) {
			// TODO @Ketheroth: 17/06/2022 find a proper way to obtain the fluid from the bucket (defer to modloader ?)
			// TODO @Ketheroth: 17/06/2022 add more check (same fluid & not full)
			this.getFluidInput().add(Fluids.WATER);
			player.setItemInHand(InteractionHand.MAIN_HAND, ItemUtils.createFilledResult(player.getMainHandItem(), player, new ItemStack(Items.BUCKET)));
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
			return true;
		} else if (item == Items.BUCKET) {
			this.getFluidInput().remove(1);
			// TODO @Ketheroth: 17/06/2022 determine the fluid according to the fluid present in the module
			player.setItemInHand(InteractionHand.MAIN_HAND, ItemUtils.createFilledResult(player.getMainHandItem(), player, new ItemStack(Items.WATER_BUCKET)));
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 2);
			return true;
		}
		return false;
	}

	@Override
	protected <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
		event.getController().setAnimation(new AnimationBuilder()
				.addAnimation("botarium.deploy", false)
				.addAnimation("botarium.idle", true));
		return PlayState.CONTINUE;
	}

	@Override
	public Map<BlockPos, SelfDeployingSlaveBlock> getMachineSlaveLocations() {
		return Map.of(this.worldPosition.above(), TechariumBlocks.SELF_DEPLOYING_SLAVE.get());
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable("container.botarium");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new BotariumMenu(id, inventory, player, this.worldPosition);
	}

	@Override
	public Map<String, InventoryModule<?>> defaultModules() {
		HashMap<String, InventoryModule<?>> map = new HashMap<>();
		map.put("inventory_input", new ItemModule(2, this));
		map.put("inventory_output", new ItemModule(4, this));
		map.put("fluid_input", new FluidModule(12));
		return map;
	}

	@Override
	public InventoryModule<?> createFromTag(CompoundTag tag, ModuleType type) {
		if (type == ModuleType.ITEM) {
			ItemModule module = new ItemModule(tag.getInt("size"), this);
			module.load(tag);
			return module;
		}
		if (type == ModuleType.FLUID) {
			FluidModule module = new FluidModule(tag.getInt("size"));
			module.load(tag);
			return module;
		}
		return InventoryModule.EMPTY;
	}

	public SimpleContainer getInput() {
		return (SimpleContainer) this.getModule("inventory_input");
	}

	public SimpleContainer getOutput() {
		return (SimpleContainer) this.getModule("inventory_output");
	}

	public FluidModule getFluidInput() {
		return (FluidModule) this.getModule("fluid_input");
	}

}
