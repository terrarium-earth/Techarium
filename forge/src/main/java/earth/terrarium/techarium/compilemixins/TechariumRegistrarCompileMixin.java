package earth.terrarium.techarium.compilemixins;

import earth.terrarium.techarium.registry.TechariumRegistrar;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = TechariumRegistrar.class, remap = false)
public class TechariumRegistrarCompileMixin<T> {
    private DeferredRegister<T> handler;

    @Overwrite
    private void init(String modId, Registry<T> registry) {
        handler = DeferredRegister.create(registry.key(), modId);
    }

    @Overwrite
    public <U extends T> Supplier<U> register(String name, Supplier<U> supplier) {
        return handler.register(name, supplier);
    }

    @Overwrite
    public void initialize() {
        handler.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
