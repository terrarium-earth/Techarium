package com.techarium.techarium.block.selfdeploying;

import com.techarium.techarium.blockentity.selfdeploying.BotariumBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class BotariumBlock extends SelfDeployingBlock {

	public BotariumBlock() {
		super(BlockBehaviour.Properties.of(Material.METAL));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BotariumBlockEntity(pos, state);
	}

	@Override
	public BoundingBox getDeployedSize() {
		return new BoundingBox(0, 0, 0, 1, 2, 1);
	}

}
