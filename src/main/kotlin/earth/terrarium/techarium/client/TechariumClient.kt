package earth.terrarium.techarium.client

import earth.terrarium.techarium.client.renderers.blocks.registerGeoRenderer
import earth.terrarium.techarium.client.renderers.items.registerGeoItem
import earth.terrarium.techarium.common.TechariumConstants
import earth.terrarium.techarium.common.registries.ModBlockEntityTypes
import earth.terrarium.techarium.common.registries.ModBlocks
import earth.terrarium.techarium.common.registries.ModItems
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent

@Mod(TechariumConstants.MOD_ID, dist = [Dist.CLIENT])
class TechariumClient(modBus: IEventBus, mod: ModContainer) {

    init {
        modBus.addListener(this::onRegisterBlockEntityRenderers)
        modBus.addListener(this::onRegisterItemExtensions)
    }

    private fun onRegisterBlockEntityRenderers(event: RegisterRenderers) {
        event.registerGeoRenderer(ModBlocks.sprinkler, ModBlockEntityTypes.sprinkler)
    }

    private fun onRegisterItemExtensions(event: RegisterClientExtensionsEvent) {
        event.registerGeoItem(ModItems.sprinkler)
    }
}