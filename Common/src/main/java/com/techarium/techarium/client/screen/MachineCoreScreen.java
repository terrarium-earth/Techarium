package com.techarium.techarium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.inventory.MachineCoreMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class MachineCoreScreen extends AbstractContainerScreen<MachineCoreMenu> {


	// TODO: 10/07/2022 show hints button
	// TODO: 10/07/2022 fix slider texture
	// TODO: 10/07/2022 increase height of the text input
	// TODO: 10/07/2022 custom font in text input ?
	// TODO: 10/07/2022 change height of buttons ? (kinda to small with the font)


	private static final ResourceLocation TEXTURE = new ResourceLocation(Techarium.MOD_ID, "textures/gui/machine_core_best.png");
	private static final Component SHOW_HINTS_TEXT = Component.translatable("gui.techarium.machine_core.hints").withStyle(Style.EMPTY);

	private static final int IDS_X = 17;
	private static final int IDS_Y = 34;
	private static final int SCROLL_X = 119; // width 6
	private static final int TEXT_INPUT_X = 17; // width 100
	private static final int TEXT_INPUT_Y = 24; // height 8

	private final List<ResourceLocation> ids;
	private final List<MultiBlockButton> buttons;
	private EditBox textInput;
	private int startIndex;
	private boolean showHints;

	public MachineCoreScreen(MachineCoreMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 145;
		this.imageHeight = 150;
		// TODO: 11/07/2022 @Ketheroth should we sort the ids in alphabetical order ?
		this.ids = this.menu.getAllMultiblockStructures();
		this.buttons = new ArrayList<>();
	}

	@Override
	protected void init() {
		super.init();
		this.addButtons("");
		this.textInput = new EditBox(this.font, this.leftPos + TEXT_INPUT_X, this.topPos + TEXT_INPUT_Y, 100, 8, Component.empty());
		this.addWidget(this.textInput);
		this.setInitialFocus(this.textInput);
		this.textInput.setFocus(true);
		this.textInput.setResponder(this::filterIds);
		this.showHints = false;
	}

	private void addButtons(String filter) {
		this.startIndex = 0;
		int y = 0;
		ResourceLocation selected = this.menu.selectedMultiblock();
		for (int i = 0; i < this.ids.size(); i++) {
			ResourceLocation id = this.ids.get(i);
			// name is created from resource location : techarium:random -> multiblock.techarium.random
			MutableComponent name = Component.translatable("multiblock." + id.getNamespace() + "." + id.getPath().replace("/", ".")).withStyle(Techarium.STYLE);
			if (filter.isEmpty() || name.getString().toLowerCase().startsWith(filter.toLowerCase())) {
				MultiBlockButton multiBlockButton = new MultiBlockButton(i, this.leftPos + IDS_X, this.topPos + IDS_Y + y, name, button -> {
					this.buttons.forEach(element -> element.selected = false);
					this.menu.setMultiBlock(id);
					((MultiBlockButton) button).selected = true;
				});
				if (id.equals(selected)) {
					multiBlockButton.selected = true;
				}
				this.addRenderableWidget(multiBlockButton);
				this.buttons.add(multiBlockButton);
				y += 10;
			}
		}
		// hide buttons outside the area
		for (int j = 8; j < this.buttons.size(); j++) {
			this.buttons.get(j).visible = false;
		}
	}

	private void filterIds(String filter) {
		//remove every button
		this.buttons.forEach(this::removeWidget);
		this.buttons.clear();
		//add back buttons starting with filter
		this.addButtons(filter);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == GLFW.GLFW_KEY_E && this.textInput.isFocused()) {
			// I don't know why, but the screen is closed even if the text input is focused
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (delta < 0) {
			// scroll down
			if (this.buttons.size() > 8 && this.startIndex + 8 < this.buttons.size()) {
				//can scroll down only if there are more buttons than possible, and we're not at the end of the buttons
				this.startIndex++;
				this.buttons.get(startIndex - 1).visible = false;
				this.buttons.get(startIndex + 7).visible = true;
				for (MultiBlockButton button : this.buttons) {
					button.y -= 10;
				}
			}
		} else if (delta > 0) {
			// scroll up
			if (this.startIndex > 0) {
				this.startIndex--;
				this.buttons.get(startIndex).visible = true;
				this.buttons.get(startIndex + 8).visible = false;
				for (MultiBlockButton button : this.buttons) {
					button.y += 10;
				}
			}
		}
		return super.mouseScrolled(mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		for (int i = 0; i < this.buttons.size(); i++) {
			if (this.buttons.get(i).mouseClicked(mouseX, mouseY, mouseButton)) {
				// when a button is clicked, send it to the server so the block-entity knows which multiblock has been selected
				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, this.buttons.get(i).index);
				return true;
			}
		}
		if (this.leftPos + 9 <= mouseX && mouseX <= this.leftPos + 23 && this.topPos + 133 <= mouseY && mouseY <= this.topPos + 147) {
			this.showHints = !this.showHints;
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		this.textInput.render(poseStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int x, int y) {
		// no labels
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		if (showHints) {
			this.blit(poseStack, this.leftPos + 9, this.topPos + 133, 0, 150, 14, 14);
		}
		this.font.draw(poseStack, SHOW_HINTS_TEXT, this.leftPos + 30, this.topPos + 136, 4210752);
		if (this.buttons.size() > 8) {
			int startX = this.leftPos + SCROLL_X;
			int startY = this.topPos + IDS_Y;
			int color = (255 << 16 | 127) << 8 | 127;
			int scrollAmount = this.buttons.size() - 8;
			int barSize = 80;
			int partSize = barSize / (scrollAmount + 1);
			RenderSystem.setShaderTexture(0, TEXTURE);
			// scroll slider 6x9
			//blit(PoseStack pPoseStack, int pX, int pY, int pBlitOffset, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureHeight, int pTextureWidth).
			int scrollStartY = startY + partSize * this.startIndex;
			//blit first line
			blit(poseStack, startX, scrollStartY, 0, 164, 6, 1);
			int yStartOffset=1;
			if (partSize%3 == 2) {
				yStartOffset = 2;
				blit(poseStack, startX, scrollStartY +1, 0, 165, 6, 3);
			}
			for (int y = scrollStartY + yStartOffset; y < startY + partSize * (this.startIndex + 1) - 2; y += 3) {
				blit(poseStack, startX, y, 0, 165, 6, 3);
			}
			//blit x 3middle line to fill between the start and the end
			//blit 2 end lines
			blit(poseStack, startX, scrollStartY + partSize - 2, this.getBlitOffset(), 0, 171, 6, 2, 256, 256);
//			fill(poseStack, startX, startY + partSize * this.startIndex, startX + 3, startY + partSize * this.startIndex + partSize, color);
		}
	}

	private class MultiBlockButton extends Button {

		private final int index;
		private boolean selected;

		public MultiBlockButton(int index, int x, int y, Component name, OnPress onPress) {
			super(x, y, 100, 10, name, onPress);
			this.index = index;
		}

		@Override
		public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.setShaderTexture(0, TEXTURE);
			int yOffset = selected ? 183 : 173;
			blit(poseStack, x, y, 0, yOffset, width, height);
			drawCenteredString(poseStack, Minecraft.getInstance().font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 16777215);
		}

	}

}
