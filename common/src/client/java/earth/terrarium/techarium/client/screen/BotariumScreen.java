package earth.terrarium.techarium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.techarium.Techarium;
import earth.terrarium.techarium.block.entity.selfdeploying.SimpleFluidContainer;
import earth.terrarium.techarium.client.util.FluidClientUtils;
import earth.terrarium.techarium.inventory.BotariumMenu;
import earth.terrarium.techarium.util.PlatformHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;

import java.util.Arrays;

public class BotariumScreen extends AbstractContainerScreen<BotariumMenu> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Techarium.MOD_ID, "textures/gui/botarium/botarium.png");
	private static final ResourceLocation COMPONENTS = new ResourceLocation(Techarium.MOD_ID, "textures/gui/botarium/botarium_components.png");

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
		this.renderFluid(poseStack);
		this.renderTooltip(poseStack, mouseX, mouseY);
	}

	private void renderFluid(PoseStack poseStack) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		if (this.menu.getFluidAmount() > 0) {
			// TODO @anyone: 17/06/2022 limit to max instead of modulo max
			// TODO @anyone: 18/06/2022 render more precisely (mb instead of bucket)
			int fluidAmount = (int) (this.menu.getFluidAmount() / SimpleFluidContainer.BUCKET_CAPACITY) % (GAUGE_HEIGHT / PX_PER_BUCKET + 1);
			int color = FluidClientUtils.getFluidColor(menu.getFluid(), menu.getBotarium().getLevel(), menu.getBotarium().getBlockPos());
			TextureAtlasSprite sprite = FluidClientUtils.getStillTexture(menu.getFluid(), menu.getBotarium().getLevel(), menu.getBotarium().getBlockPos());
			RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >> 24 & 255) / 255.0F);
			RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
			int y = GAUGE_BOTTOM - fluidAmount * PX_PER_BUCKET;
			// TODO @anyone: 18/06/2022 blit the sprite nicely so it isn't stretched-out
			blit(poseStack, this.leftPos + GAUGE_X, this.topPos + y, this.getBlitOffset(), GAUGE_WIDTH, fluidAmount * PX_PER_BUCKET, sprite);
		}
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.setShaderTexture(0, COMPONENTS);
		blit(poseStack, this.leftPos + GAUGE_X - 1, this.topPos + GAUGE_TOP - 1, 0, 50, 14, 50);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int x, int y) {
		this.font.draw(poseStack, this.title, 13, 8, 16777215);
	}

	@Override
	protected void renderTooltip(PoseStack poseStack, int mouseX, int mouseY) {
		super.renderTooltip(poseStack, mouseX, mouseY);
		if (this.menu.getFluidAmount() <= 0) {
			return;
		}

		int kekieBuckets = (int) Math.round(menu.getFluidAmount() * SimpleFluidContainer.TO_MILLI_BUCKETS);
		// should it display only if the cursor is on only on the fluid (and not on the gauge) ?
		if (this.leftPos + GAUGE_X <= mouseX && mouseX <= this.leftPos + GAUGE_X + GAUGE_WIDTH
				&& this.topPos + GAUGE_TOP <= mouseY && mouseY <= this.topPos + GAUGE_TOP + GAUGE_HEIGHT) {
			this.renderComponentTooltip(poseStack, Arrays.asList(PlatformHelper.getFluidName(this.menu.getFluid()), Component.literal(kekieBuckets + " kekie-buckets")), mouseX, mouseY);
		}
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, TEXTURE);
		this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

}
