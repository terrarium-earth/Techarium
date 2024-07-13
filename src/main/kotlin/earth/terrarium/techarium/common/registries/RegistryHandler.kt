package earth.terrarium.techarium.common.registries

internal fun initializeRegistries() {
    ModComponents.registry.init()
    ModBlocks.registry.init()
    ModBlockEntityTypes.registry.init()
}