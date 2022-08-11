package com.techarium.techarium.registry;

import com.techarium.techarium.util.extensions.ExtendableDeclaration;
import net.minecraft.core.Registry;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

@ExtendableDeclaration
public class TechariumRegistrar<T> {
    @ExtendableDeclaration
    public TechariumRegistrar(String modId, Registry<T> registry) {
        throw new NotImplementedException("TechariumRegistrar was not implemented.");
    }

    @ExtendableDeclaration
    public <U extends T> Supplier<U> register(String name, Supplier<U> supplier) {
        throw new NotImplementedException("register was not implemented.");
    }

    @ExtendableDeclaration
    public void initialize() {
        throw new NotImplementedException("initialize was not implemented.");
    }
}
