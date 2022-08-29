package earth.terrarium.techarium.forge.client.extensions;

import earth.terrarium.techarium.client.util.FluidClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;
import org.jetbrains.annotations.Nullable;

@ClassExtension(FluidClientUtils.class)
public class FluidClientUtilsImpl {
    @ImplementsBaseElement
    public static TextureAtlasSprite getStillTexture(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation location = view == null || pos == null ? extensions.getStillTexture() : extensions.getStillTexture(view.getFluidState(pos), view, pos);
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
    }

    @ImplementsBaseElement
    public static int getFluidColor(Fluid fluid, @Nullable BlockAndTintGetter view, @Nullable BlockPos pos) {
        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid);
        return view == null || pos == null ? extensions.getTintColor() : extensions.getTintColor(view.getFluidState(pos), view, pos);
    }
}
