package earth.terrarium.techarium.fabric.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Supplier;

public class RegistrySupplier<T, U extends T> implements Supplier<U> {
    private final ResourceLocation key;
    private final Registry<T> registry;
    private final Supplier<U> factory;

    private U value;

    public RegistrySupplier(ResourceLocation key, Registry<T> registry, Supplier<U> factory) {
        this.key = key;
        this.registry = registry;
        this.factory = factory;
    }

    public void initialize() {
        value = Registry.register(registry, key, factory.get());
    }

    @Override
    public U get() {
        return Objects.requireNonNull(value, () -> "Registry Object not present: " + key);
    }
}
