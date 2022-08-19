package earth.terrarium.techarium.fabric.extensions;

import earth.terrarium.techarium.fabric.registry.RegistrySupplier;
import earth.terrarium.techarium.registry.TechariumRegistrar;
import earth.terrarium.techarium.util.extensions.ExtensionFor;
import earth.terrarium.techarium.util.extensions.ExtensionImplementation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ExtensionFor(TechariumRegistrar.class)
public class TechariumFabricRegistrar<T> {
    private final String modId;
    private final Registry<T> registry;

    private final List<RegistrySupplier<T, ?>> registered = new ArrayList<>();

    @ExtensionImplementation
    public TechariumFabricRegistrar(String modId, Registry<T> registry) {
        this.modId = modId;
        this.registry = registry;
    }

    @ExtensionImplementation
    public <U extends T> Supplier<U> register(String name, Supplier<U> factory) {
        var supplier = new RegistrySupplier<>(new ResourceLocation(modId, name), registry, factory);

        registered.add(supplier);
        return supplier;
    }

    @ExtensionImplementation
    public void initialize() {
        for (var entry : registered) {
            entry.initialize();
        }

        registered.clear();
    }
}
