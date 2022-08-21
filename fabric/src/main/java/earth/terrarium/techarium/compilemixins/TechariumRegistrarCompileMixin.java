package earth.terrarium.techarium.compilemixins;

import earth.terrarium.techarium.fabric.registry.RegistrySupplier;
import earth.terrarium.techarium.registry.TechariumRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(value = TechariumRegistrar.class, remap = false)
public class TechariumRegistrarCompileMixin<T> {
    private String modId;

    private Registry<T> registry;

    private final List<RegistrySupplier<T, ?>> registered = new ArrayList<>();

    @Overwrite
    private void init(String modId, Registry<T> registry) {
        this.modId = modId;
        this.registry = registry;
    }

    @Overwrite
    public <U extends T> Supplier<U> register(String name, Supplier<U> factory) {
        var supplier = new RegistrySupplier<>(new ResourceLocation(modId, name), registry, factory);

        registered.add(supplier);
        return supplier;
    }

    @Overwrite
    public void initialize() {
        for (var entry : registered) {
            entry.initialize();
        }

        registered.clear();
    }
}
