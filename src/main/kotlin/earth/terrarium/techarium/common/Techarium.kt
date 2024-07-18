package earth.terrarium.techarium.common

import earth.terrarium.techarium.common.registries.ModBlockEntityTypes
import earth.terrarium.techarium.common.registries.initializeRegistries
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent

@Mod(TechariumConstants.MOD_ID)
class Techarium(modBus: IEventBus, mod: ModContainer) {

    init {
        initializeRegistries()

        modBus.register(this)
    }

    @SubscribeEvent
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
       event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ModBlockEntityTypes.sprinkler) { it, _ -> it.tank }
    }
}