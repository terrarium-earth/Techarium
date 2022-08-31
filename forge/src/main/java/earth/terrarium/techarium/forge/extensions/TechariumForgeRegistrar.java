package earth.terrarium.techarium.forge.extensions;

import earth.terrarium.techarium.registry.TechariumRegistrar;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.msrandom.extensions.annotations.ClassExtension;
import net.msrandom.extensions.annotations.ImplementsBaseElement;

import java.util.function.Supplier;

@ClassExtension(TechariumRegistrar.class)
public class TechariumForgeRegistrar<T> {
    private final DeferredRegister<T> handler;

    @ImplementsBaseElement
    public TechariumForgeRegistrar(String modId, Registry<T> registry) {
        handler = DeferredRegister.create(registry.key(), modId);
    }

    @ImplementsBaseElement
    public <U extends T> Supplier<U> register(String name, Supplier<U> supplier) {
        return handler.register(name, supplier);
    }

    @ImplementsBaseElement
    public void initialize() {
        handler.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
