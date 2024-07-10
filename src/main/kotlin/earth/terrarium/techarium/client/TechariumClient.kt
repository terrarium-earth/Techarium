package earth.terrarium.techarium.client

import earth.terrarium.techarium.common.TechariumConstants
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(TechariumConstants.MOD_ID, dist = [Dist.CLIENT])
class TechariumClient(modBus: IEventBus, mod: ModContainer) {

    init {
        println("Hello, ${mod.modInfo.displayName} v${mod.modInfo.version} (client)")
    }
}