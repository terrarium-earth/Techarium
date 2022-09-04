package earth.terrarium.techarium.mixin;

import earth.terrarium.techarium.util.MagnetTarget;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntity.class)
public class MixinItemEntity implements MagnetTarget {

    private boolean Techarium$inContainer;
    private boolean Techarium$isTarget;
    private float Techarium$progress;


    @Override
    public void Techarium$setInContainer(boolean value) {
        this.Techarium$inContainer = value;
    }

    @Override
    public boolean Techarium$inContainer() {
        return this.Techarium$inContainer;
    }

    @Override
    public void Techarium$setIsTarget(boolean value) {
        this.Techarium$isTarget = value;
    }

    @Override
    public boolean Techarium$isTarget() {
        return this.Techarium$isTarget;
    }

    @Override
    public void Techarium$setProgress(float value) {
        this.Techarium$progress = value;
    }

    @Override
    public float Techarium$getProgress() {
        return this.Techarium$progress;
    }
}
