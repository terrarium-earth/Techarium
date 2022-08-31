package earth.terrarium.techarium.registry;

import net.msrandom.extensions.annotations.ImplementedByExtension;
import net.minecraft.core.Registry;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class TechariumRegistrar<T> {
    @ImplementedByExtension
    public TechariumRegistrar(String modId, Registry<T> registry) {
        throw new NotImplementedException("TechariumRegistrar was not implemented.");
    }

    @ImplementedByExtension
    public <U extends T> Supplier<U> register(String name, Supplier<U> supplier) {
        throw new NotImplementedException("register was not implemented.");
    }

    @ImplementedByExtension
    public void initialize() {
        throw new NotImplementedException("initialize was not implemented.");
    }
}
