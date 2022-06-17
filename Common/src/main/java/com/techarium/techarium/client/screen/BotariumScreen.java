package com.techarium.techarium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.block.inventory.BotariumMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BotariumScreen extends AbstractContainerScreen<BotariumMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Techarium.MOD_ID, "textures/gui/botarium/botarium.png");
	private static final int GAUGE_X = 24;
	private static final int GAUGE_TOP = 35;
	private static final int GAUGE_BOTTOM = 83;
	private static final int GAUGE_WIDTH = 12;
	private static final int GAUGE_HEIGHT = 48;
	private static final int PX_PER_BUCKET = 4;

	public BotariumScreen(BotariumMenu menu, Inventory inventory, Component name) {
		super(menu, inventory, name);
		this.imageWidth = 172;
		this.imageHeight = 184;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderFluid(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	private void renderFluid(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		if (this.menu.getFluidAmount() > 0) {
			int fluidAmount = this.menu.getFluidAmount() % (GAUGE_HEIGHT / PX_PER_BUCKET + 1);  // TODO: 17/06/2022 limit to max instead of mod max
			int color = argbToInt(255, 0, 0, 255);
			for (int i = 0; i < fluidAmount; i++) {
				int y = GAUGE_BOTTOM - (i + 1) * PX_PER_BUCKET;
				// TODO @anyone: 17/06/2022 render the fluid instead of a blue rectangle
				fill(poseStack, relX + GAUGE_X, relY + y, relX + GAUGE_X + GAUGE_WIDTH, relY + y + PX_PER_BUCKET, color);
			}
		}
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int x, int y) {
		// no labels
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		this.blit(poseStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);
	}

	private int argbToInt(int a, int r, int g, int b) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

}
