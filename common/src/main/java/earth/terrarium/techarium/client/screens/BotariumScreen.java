package earth.terrarium.techarium.client.screens;

import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.client.screens.base.MachineScreen;
import earth.terrarium.techarium.common.blockentities.machines.BotariumBlockEntity;
import earth.terrarium.techarium.common.menus.machines.BotariumMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BotariumScreen extends MachineScreen<BotariumMenu, BotariumBlockEntity> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Techarium.MOD_ID, "textures/gui/container/botarium.png");

    public BotariumScreen(BotariumMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component, TEXTURE, 203, 184);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.render(graphics, mouseX, mouseY, f);
        this.drawEnergyBar(graphics, mouseX, mouseY, -6, 66, entity.getEnergyStorage(), entity.energyDifference());
        this.drawFluidBar(graphics, mouseX, mouseY, 9, 65, entity.getFluidContainer(), 0, entity.fluidDifference());
    }
}
