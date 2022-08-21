package earth.terrarium.techarium.registry;

import earth.terrarium.techarium.util.ImplementedByMixin;
import net.minecraft.core.Registry;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class TechariumRegistrar<T> {
    public TechariumRegistrar(String modId, Registry<T> registry) {
        init(modId, registry);
    }

    @ImplementedByMixin
    private void init(String modId, Registry<T> registry) {
        throw new NotImplementedException("init was not implemented.");
    }

    @ImplementedByMixin
    public <U extends T> Supplier<U> register(String name, Supplier<U> supplier) {
        throw new NotImplementedException("register was not implemented.");
    }

    @ImplementedByMixin
    public void initialize() {
        throw new NotImplementedException("initialize was not implemented.");
    }
}
