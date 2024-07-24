package earth.terrarium.techarium.client.renderers.blocks

import com.teamresourceful.resourcefullibkt.common.id
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoBlockRenderer

class BaseGeoBlockRenderer<T>(
    block: Block
) : GeoBlockRenderer<T>(DefaultedBlockGeoModel(block.id)) where T : GeoAnimatable, T : BlockEntity

fun <T> EntityRenderersEvent.RegisterRenderers.registerGeoRenderer(
    block: Block,
    entity: BlockEntityType<T>
) where T : BlockEntity, T : GeoAnimatable {
    this.registerBlockEntityRenderer(entity) { BaseGeoBlockRenderer(block) }
}