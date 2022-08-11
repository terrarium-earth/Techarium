package com.techarium.techarium.fabric.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class RegistrySupplier<T, U extends T> implements Supplier<U> {
    private final Registry<T> registry;
    private final Supplier<U> factory;

    private U value;

    public RegistrySupplier(Registry<T> registry, Supplier<U> factory) {
        this.registry = registry;
        this.factory = factory;
    }

    public void initialize(ResourceLocation key) {
        value = Registry.register(registry, key, factory.get());
    }

    @Override
    public U get() {
        return value;
    }
}
