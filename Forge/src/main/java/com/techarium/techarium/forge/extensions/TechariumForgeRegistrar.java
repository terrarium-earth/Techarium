package com.techarium.techarium.forge.extensions;

import com.techarium.techarium.registry.TechariumRegistrar;
import com.techarium.techarium.util.extensions.ExtensionFor;
import com.techarium.techarium.util.extensions.ExtensionImplementation;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

@ExtensionFor(TechariumRegistrar.class)
public class TechariumForgeRegistrar<T> {
    private final DeferredRegister<T> handler;

    @ExtensionImplementation
    public TechariumForgeRegistrar(String modId, Registry<T> registry) {
        handler = DeferredRegister.create(registry.key(), modId);
    }

    @ExtensionImplementation
    public <U extends T> Supplier<U> register(String name, Supplier<U> supplier) {
        return handler.register(name, supplier);
    }

    @ExtensionImplementation
    public void initialize() {
        handler.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
