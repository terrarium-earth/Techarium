package earth.terrarium.techarium.common

import earth.terrarium.techarium.common.registries.initializeRegistries
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(TechariumConstants.MOD_ID)
class Techarium(modBus: IEventBus, mod: ModContainer) {

    init {
        println("Hello, ${mod.modInfo.displayName} v${mod.modInfo.version} (common)")

        initializeRegistries()
    }
}