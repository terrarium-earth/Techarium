package com.techarium.techarium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.techarium.techarium.Techarium;
import com.techarium.techarium.client.widget.MultiBlockButton;
import com.techarium.techarium.inventory.MachineCoreMenu;
import com.techarium.techarium.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MachineCoreScreen extends AbstractContainerScreen<MachineCoreMenu> {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Techarium.MOD_ID, "textures/gui/machine_core.png");
	private static final Component SHOW_HINTS_TEXT = Component.translatable("gui.techarium.machine_core.hints");

	private static final int IDS_X = 17;
	private static final int IDS_Y = 38;
	private static final int SCROLL_X = 119; // width 6
	private static final int TEXT_INPUT_X = 19; // width 100
	private static final int TEXT_INPUT_Y = 26; // height 12

	private final List<ResourceLocation> ids;
	private final List<MultiBlockButton> buttons;
	private EditBox textInput;
	private int startIndex;
	private boolean showHints;

	public MachineCoreScreen(MachineCoreMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
		this.imageWidth = 165;
		this.imageHeight = 186;
		this.ids = this.menu.getAllMultiblockStructures();
		this.buttons = new ArrayList<>();
	}

	@Override
	protected void init() {
		super.init();
		this.initButtons("");
		this.textInput = new EditBox(this.font, this.leftPos + TEXT_INPUT_X, this.topPos + TEXT_INPUT_Y, 100, 12, Component.empty());
		this.addRenderableWidget(this.textInput);
		this.setInitialFocus(this.textInput);
		this.textInput.setFocus(true);
		this.textInput.setBordered(false);
		this.textInput.setResponder(this::filterIds);
		this.showHints = false;
	}

	/**
	 * Initialize the selectable buttons for each multiblock.
	 *
	 * @param filter multiblock name filter (do nothing if empty).
	 */
	private void initButtons(String filter) {
		this.startIndex = 0;
		this.buttons.forEach(this::removeWidget);
		this.buttons.clear();
		Optional<ResourceLocation> selected = this.menu.selectedMultiblock();
		ArrayList<MultiBlockButton> list = new ArrayList<>();
		for (int i = 0; i < this.ids.size(); i++) {
			ResourceLocation id = this.ids.get(i);
			// name is created from resource location : techarium:random -> multiblock.techarium.random
			MutableComponent name = Utils.translatableComponent("multiblock." + id.getNamespace() + "." + id.getPath().replace("/", "."));
			if (filter.isEmpty() || name.getString().toLowerCase().startsWith(filter.toLowerCase())) {
				MultiBlockButton multiBlockButton = new MultiBlockButton(i, this.leftPos + IDS_X, 0, 100, 14, name, button -> {
					this.buttons.forEach(element -> element.setSelected(false));
					this.menu.setMultiBlock(id);
					((MultiBlockButton) button).setSelected(true);
				});
				if (selected.isPresent()) {
					if (id.equals(selected.get())) {
						multiBlockButton.setSelected(true);
					}
				}
				list.add(multiBlockButton);
			}
		}
		// sort the buttons by the translated name of the multiblocks
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			MultiBlockButton elem = list.get(i);
			elem.y = this.topPos + IDS_Y + 14 * i;
		}
		this.buttons.addAll(list.stream().sorted().toList());
		this.buttons.forEach(this::addRenderableWidget);
		// hide buttons outside the area
		for (int j = 8; j < this.buttons.size(); j++) {
			this.buttons.get(j).visible = false;
		}
	}

	private void filterIds(String filter) {
		this.initButtons(filter);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.minecraft.options.keyInventory.matches(keyCode, scanCode) && this.textInput.isFocused()) {
//			 I don't know why, but the screen is closed even if the text input is focused
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
					button.y -= 14;
				}
			}
		} else if (delta > 0) {
			// scroll up
			if (this.startIndex > 0) {
				this.startIndex--;
				this.buttons.get(startIndex).visible = true;
				this.buttons.get(startIndex + 8).visible = false;
				for (MultiBlockButton button : this.buttons) {
					button.y += 14;
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
				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, this.buttons.get(i).getIndex());
				return true;
			}
		}
		if (this.leftPos + 9 <= mouseX && mouseX <= this.leftPos + 23 && this.topPos + 169 <= mouseY && mouseY <= this.topPos + 183) {
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
		this.font.draw(poseStack, this.title, 6, 3, 16777215);
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
		// display scrollbar
		if (this.buttons.size() > 8) {
			int startX = this.leftPos + SCROLL_X;
			int startY = this.topPos + IDS_Y;
			int scrollAmount = this.buttons.size() - 8;
			int barSize = 112;
			int partSize = barSize / (scrollAmount + 1);
			if (partSize % 2 == 1) {
				partSize--;
			}
			int scrollStartY = startY + partSize * this.startIndex;
			//blit first line
			blit(poseStack, startX, scrollStartY, 14, 201, 6, 1);
			int yStartOffset = 1;
			// blit the middle line to fill between the start and the end
			for (int y = scrollStartY + yStartOffset; y < startY + partSize * (this.startIndex + 1) - 3; y += 2) {
				blit(poseStack, startX, y, 14, 202, 6, 2);
			}
			// blit the 2 end lines
			blit(poseStack, startX, scrollStartY + partSize - 3, 14, 207, 6, 3);
		}
		// display hint button
		if (this.showHints) {
			this.blit(poseStack, this.leftPos + 9, this.topPos + 169, 0, 196, 14, 14);
		}
		this.font.draw(poseStack, SHOW_HINTS_TEXT, this.leftPos + 31, this.topPos + 172, 4210752);
	}

}
