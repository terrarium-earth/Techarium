package earth.terrarium.techarium.datagen

import earth.terrarium.techarium.common.TechariumConstants
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(TechariumConstants.MOD_ID)
class TechariumDatagen(modBus: IEventBus, mod: ModContainer) {
    init {
        modBus.register(DatagenHandler)
    }
}