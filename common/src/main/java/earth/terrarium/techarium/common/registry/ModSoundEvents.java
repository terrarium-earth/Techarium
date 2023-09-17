package earth.terrarium.techarium.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.techarium.Techarium;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSoundEvents {
    public static final ResourcefulRegistry<SoundEvent> SOUND_EVENTS = ResourcefulRegistries.create(BuiltInRegistries.SOUND_EVENT, Techarium.MOD_ID);

    public static final RegistryEntry<SoundEvent> BOTARIUM_DEPLOY = SOUND_EVENTS.register("botarium_deploy", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Techarium.MOD_ID, "botarium_deploy")));
}
