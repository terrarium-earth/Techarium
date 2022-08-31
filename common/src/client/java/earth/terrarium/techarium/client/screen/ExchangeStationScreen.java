package earth.terrarium.techarium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.inventory.ExchangeStationMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ExchangeStationScreen extends AbstractContainerScreen<ExchangeStationMenu> {

    private final ResourceLocation TEXTURE = new ResourceLocation(Techarium.MOD_ID, "textures/gui/exchange_station.png");

    public ExchangeStationScreen(ExchangeStationMenu menu, Inventory inventory, Component name) {
        super(menu, inventory, name);
        this.imageWidth = 193;
        this.imageHeight = 230;
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
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

}
