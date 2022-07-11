package com.techarium.techarium.client.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.blockentity.multiblock.MachineCoreBlockEntity;
import com.techarium.techarium.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Techarium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

	@SubscribeEvent
	public static void onRenderLevelLast(RenderLevelLastEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		if (Minecraft.getInstance().level == null) {
			return;
		}
		PoseStack pose = event.getPoseStack();
		pose.pushPose();
		for (BlockPos corePos : MachineCoreBlockEntity.CORE_WITH_OBSTRUCTION) {
			BlockEntity blockEntity = Minecraft.getInstance().level.getBlockEntity(corePos);
			if (blockEntity instanceof MachineCoreBlockEntity machineCoreBlockEntity) {
				for (BlockPos obstructingBlock : machineCoreBlockEntity.getObstructingBlocks(Minecraft.getInstance().level, corePos)) {
					RenderUtil.displayRedOutline(pose, obstructingBlock.getX(), obstructingBlock.getY(), obstructingBlock.getZ());
				}
			}
		}
		pose.popPose();
	}

}
