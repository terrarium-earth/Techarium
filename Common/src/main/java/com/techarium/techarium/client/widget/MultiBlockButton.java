package com.techarium.techarium.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.techarium.techarium.client.screen.MachineCoreScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class MultiBlockButton extends Button implements Comparable<MultiBlockButton> {

	/**
	 * The index in the multiblock registry.
	 */
	private final int index;
	/**
	 * If the button is selected.
	 */
	private boolean selected;

	public MultiBlockButton(int index, int x, int y, int width, int height, Component name, OnPress onPress) {
		super(x, y, width, height, name, onPress);
		this.index = index;
	}

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, MachineCoreScreen.TEXTURE);
		int yOffset = selected ? 224 : 210;
		blit(poseStack, x, y, 0, yOffset, width, height);
		Minecraft.getInstance().font.draw(poseStack, this.getMessage(), this.x + 3, this.y + 3, 16777215);
	}

	@Override
	public int compareTo(MultiBlockButton other) {
		return this.getMessage().getString().compareTo(other.getMessage().getString());
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getIndex() {
		return this.index;
	}

}
