package earth.terrarium.techarium.client.renderers.items

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.teamresourceful.resourcefullibkt.client.pushPop
import com.teamresourceful.resourcefullibkt.common.id
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.item.BlockItem
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.cache.`object`.BakedGeoModel
import software.bernie.geckolib.model.DefaultedBlockGeoModel
import software.bernie.geckolib.renderer.GeoItemRenderer

class BaseGeoBlockItemRenderer<T>(
    item: T
) : GeoItemRenderer<T>(DefaultedBlockGeoModel(item.id)) where T : GeoAnimatable, T : BlockItem {

    override fun actuallyRender(
        poseStack: PoseStack,
        animatable: T,
        model: BakedGeoModel?,
        renderType: RenderType?,
        bufferSource: MultiBufferSource?,
        buffer: VertexConsumer?,
        isReRender: Boolean,
        partialTick: Float,
        packedLight: Int,
        packedOverlay: Int,
        colour: Int
    ) {
        poseStack.pushPop {
            translate(0.0, -0.5, 0.0)
            super.actuallyRender(
                poseStack,
                animatable,
                model,
                renderType,
                bufferSource,
                buffer,
                isReRender,
                partialTick,
                packedLight,
                packedOverlay,
                colour
            )
        }
    }
}

class GeoRendererClientExtension<T>(
    val item: T
) : IClientItemExtensions where T : GeoAnimatable, T : BlockItem {

    private val renderer by lazy { BaseGeoBlockItemRenderer(item) }

    override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
        return renderer
    }
}

fun <T> RegisterClientExtensionsEvent.registerGeoItem(item: T) where T : GeoAnimatable, T : BlockItem {
    this.registerItem(GeoRendererClientExtension(item), item)
}