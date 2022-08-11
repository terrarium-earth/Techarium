package com.techarium.techarium.fabric.extensions;

import com.techarium.techarium.fabric.registry.RegistrySupplier;
import com.techarium.techarium.registry.TechariumRegistrar;
import com.techarium.techarium.util.extensions.ExtensionFor;
import com.techarium.techarium.util.extensions.ExtensionImplementation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

@ExtensionFor(TechariumRegistrar.class)
public class TechariumFabricRegistrar<T> {
    private final String modId;
    private final Registry<T> registry;

    private final Map<ResourceLocation, RegistrySupplier<T, ?>> registered = new LinkedHashMap<>();

    @ExtensionImplementation
    public TechariumFabricRegistrar(String modId, Registry<T> registry) {
        this.modId = modId;
        this.registry = registry;
    }

    @ExtensionImplementation
    public <U extends T> Supplier<U> register(String name, Supplier<U> factory) {
        var supplier = new RegistrySupplier<>(registry, factory);
        registered.put(new ResourceLocation(modId, name), supplier);

        return supplier;
    }

    @ExtensionImplementation
    public void initialize() {
        for (var entry : registered.entrySet()) {
            entry.getValue().initialize(entry.getKey());
        }

        registered.clear();
    }
}
