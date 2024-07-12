package earth.terrarium.techarium.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import earth.terrarium.techarium.common.blocks.entities.ComponentBlockEntity;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {

    @Inject(method = "lambda$loadWithComponents$1", at = @At("HEAD"), cancellable = true)
    private void loadWithComponents(DataComponentMap components, CallbackInfo ci) {
        Object thisObject = this;
        //noinspection ConstantValue
        if (!(thisObject instanceof ComponentBlockEntity be)) return;
        ci.cancel();
        be.setComponents(components);
    }

    @Inject(
        method = "applyComponents",
        at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/core/component/DataComponentPatch;split()Lnet/minecraft/core/component/DataComponentPatch$SplitResult;"
        ),
        cancellable = true
    )
    private void applyComponents(CallbackInfo ci, @Local(ordinal = 1) DataComponentPatch components) {
        Object thisObject = this;
        //noinspection ConstantValue
        if (!(thisObject instanceof ComponentBlockEntity be)) return;
        ci.cancel();
        be.applyComponents(components);
    }
}
