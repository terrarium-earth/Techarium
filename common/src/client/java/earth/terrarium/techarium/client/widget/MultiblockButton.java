package earth.terrarium.techarium.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.techarium.block.entity.multiblock.MachineCoreBlockEntity;
import earth.terrarium.techarium.client.screen.MachineCoreScreen;
import earth.terrarium.techarium.multiblock.MultiblockStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class MultiblockButton extends Button {
	private final MachineCoreBlockEntity machineCore;
	private final MultiblockStructure multiblock;

	public MultiblockButton(MachineCoreBlockEntity machineCore, MultiblockStructure multiblock, int x, int y, int width, int height, Component name, OnPress onPress) {
		super(x, y, width, height, name, onPress);
		this.machineCore = machineCore;
		this.multiblock = multiblock;
	}

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, MachineCoreScreen.TEXTURE);
		int yOffset = multiblock == machineCore.selectedMultiblock() ? 224 : 210;
		blit(poseStack, x, y, 0, yOffset, width, height);
		Minecraft.getInstance().font.draw(poseStack, this.getMessage(), this.x + 3, this.y + 3, 16777215);
	}

	public MultiblockStructure getMultiblock() {
		return multiblock;
	}
}
