package earth.terrarium.techarium.mixins.fabric.common;

import earth.terrarium.techarium.client.TechariumClient;
import earth.terrarium.techarium.common.items.base.CustomGeoBlockItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Mixin(CustomGeoBlockItem.class)
public abstract class CustomGeoBlockItemMixin extends Item implements GeoItem {

    @Unique
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public CustomGeoBlockItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
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

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }
}