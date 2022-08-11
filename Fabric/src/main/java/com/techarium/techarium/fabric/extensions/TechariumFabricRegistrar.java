package com.techarium.techarium.fabric.extensions;

import com.techarium.techarium.fabric.registry.RegistrySupplier;
import com.techarium.techarium.registry.TechariumRegistrar;
import com.techarium.techarium.util.extensions.ExtensionFor;
import com.techarium.techarium.util.extensions.ExtensionImplementation;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
