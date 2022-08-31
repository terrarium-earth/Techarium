package earth.terrarium.techarium.fabric.extensions;

import earth.terrarium.techarium.fabric.registry.RegistrySupplier;
import earth.terrarium.techarium.registry.TechariumRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ClassExtension(TechariumRegistrar.class)
public class TechariumFabricRegistrar<T> {
    private final String modId;
    private final Registry<T> registry;

    private final List<RegistrySupplier<T, ?>> registered = new ArrayList<>();

    @ImplementsBaseElement
    public TechariumFabricRegistrar(String modId, Registry<T> registry) {
        this.modId = modId;
        this.registry = registry;
    }

    @ImplementsBaseElement
    public <U extends T> Supplier<U> register(String name, Supplier<U> factory) {
        var supplier = new RegistrySupplier<>(new ResourceLocation(modId, name), registry, factory);

        registered.add(supplier);
        return supplier;
    }

    @ImplementsBaseElement
    public void initialize() {
        for (var entry : registered) {
            entry.initialize();
        }

        registered.clear();
    }
}
