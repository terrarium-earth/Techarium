package earth.terrarium.techarium.client.screens.base;

import com.teamresourceful.resourcefullib.client.screens.AbstractContainerCursorScreen;
import earth.terrarium.botarium.common.energy.impl.WrappedBlockEnergyContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.techarium.client.utils.GuiUtils;
import earth.terrarium.techarium.common.blockentities.base.ContainerMachineBlockEntity;
import earth.terrarium.techarium.common.constants.ConstantComponents;
import earth.terrarium.techarium.common.menus.base.BasicContainerMenu;
import earth.terrarium.techarium.common.networking.NetworkHandler;
import earth.terrarium.techarium.common.networking.messages.ServerboundClearFluidTankPacket;
import earth.terrarium.techarium.common.utils.ComponentUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineScreen<T extends BasicContainerMenu<U>, U extends ContainerMachineBlockEntity> extends AbstractContainerCursorScreen<T> {
    private final ResourceLocation texture;
    private final List<ClearOnClick> clearOnClicks = new ArrayList<>();

    protected U entity;

    public MachineScreen(T menu, Inventory inventory, Component component, ResourceLocation texture, int width, int height) {
        super(menu, inventory, component);
        this.texture = texture;
        this.imageWidth = width;
        this.imageHeight = height;
        this.entity = menu.getEntity();

        this.inventoryLabelX = width - 180;
        this.inventoryLabelY = height - 98;
        this.titleLabelX = width - 180;
        this.titleLabelY = 43;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, f);
        renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        graphics.blit(this.texture, left - 8, top, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
    }

    public int getTextColor() {
        return 0x2a262b;
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // TODO
//        graphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, this.getTextColor(), false);
//        graphics.drawString(font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.getTextColor(), false);
    }

    public void drawEnergyBar(GuiGraphics graphics, int mouseX, int mouseY, int xOffset, int yOffset, WrappedBlockEnergyContainer energy, long energyDifference) {
        GuiUtils.drawEnergyBar(
            graphics, mouseX,
            mouseY, font,
            this.leftPos + xOffset,
            this.topPos + yOffset,
            energy,
            ComponentUtils.getEnergyDifferenceComponent(energyDifference),
            ComponentUtils.getMaxEnergyInComponent(energy.maxInsert()),
            ComponentUtils.getMaxEnergyOutComponent(energy.maxExtract()));
    }

    public void drawFluidBar(GuiGraphics graphics, int mouseX, int mouseY, int xOffset, int yOffset, WrappedBlockFluidContainer fluid, int tank, long fluidDifference) {
        int x = this.leftPos + xOffset;
        int y = this.topPos + yOffset;
        GuiUtils.drawFluidBar(
            graphics, mouseX,
            mouseY, font,
            x, y,
            fluid, tank,
            ComponentUtils.getFluidDifferenceComponent(fluidDifference),
            !fluid.isEmpty() ? ConstantComponents.CLEAR_FLUID_TANK : Component.empty());

        clearOnClicks.add(new ClearOnClick(x + 6, y - 31, x + 22, y + 17, tank));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (var clearOnClick : clearOnClicks) {
            if (mouseX >= clearOnClick.minX() && mouseX <= clearOnClick.maxX() && mouseY >= clearOnClick.minY() && mouseY <= clearOnClick.maxY()) {
                if (button == 1 && Screen.hasShiftDown()) {
                    NetworkHandler.CHANNEL.sendToServer(new ServerboundClearFluidTankPacket(entity.getBlockPos(), clearOnClick.tank()));
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        setFocused(null);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @SuppressWarnings("unused")
    public void testClickArea(GuiGraphics graphics, Rect2i clickArea) {
        graphics.fill(leftPos + clickArea.getX(), topPos + clickArea.getY(), leftPos + clickArea.getX() + clickArea.getWidth(), topPos + clickArea.getY() + clickArea.getHeight(), 0x40ffff00);
    }

    private record ClearOnClick(int minX, int minY, int maxX, int maxY, int tank) {}
}
