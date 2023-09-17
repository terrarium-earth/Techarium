package earth.terrarium.techarium.mixins.forge.common;

import earth.terrarium.techarium.client.TechariumClient;
import earth.terrarium.techarium.common.items.base.CustomGeoBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(CustomGeoBlockItem.class)
public abstract class CustomGeoBlockItemMixin extends Item {

    public CustomGeoBlockItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = TechariumClient.getItemRenderer(CustomGeoBlockItemMixin.this);
                }

                return this.renderer;
            }
        });
    }
}
