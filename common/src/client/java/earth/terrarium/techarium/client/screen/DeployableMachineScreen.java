package earth.terrarium.techarium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.techarium.machine.DeployableMachineMenu;
import earth.terrarium.techarium.machine.definition.MachineGUIDefinition;
import earth.terrarium.techarium.machine.definition.MachineModuleGUIDefinition;
import earth.terrarium.techarium.registry.RegistryHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DeployableMachineScreen extends AbstractContainerScreen<DeployableMachineMenu> {

    private MachineGUIDefinition guiDefinition;

    public DeployableMachineScreen(DeployableMachineMenu menu, Inventory inventory, Component name) {
        super(menu, inventory, name);
        inventory.player.level.registryAccess()
                .registry(RegistryHelper.getMachineGUIDefinitionRegistryKey())
                .ifPresent(machineGUIDefinitions -> this.guiDefinition = machineGUIDefinitions.get(menu.getMachine().getMachineId()));
        if (this.guiDefinition != null) {
            this.imageWidth = guiDefinition.width();
            this.imageHeight = guiDefinition.height();
            this.leftPos = guiDefinition.inventoryX();
            this.topPos = guiDefinition.inventoryY();
        }
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int x, int y) {
        this.font.draw(poseStack, this.title, 6, 3, 16777215);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        if (guiDefinition != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, guiDefinition.texture());
            this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            for (MachineModuleGUIDefinition module : guiDefinition.guiModuleDefinitions()) {
                module.textureOverride().ifPresent(texture -> RenderSystem.setShaderTexture(0, texture));
                this.blit(poseStack, this.leftPos + module.x(), this.topPos + module.y(), module.xOffset(), module.yOffset(), module.width(), module.height());
            }
        }
    }

}
