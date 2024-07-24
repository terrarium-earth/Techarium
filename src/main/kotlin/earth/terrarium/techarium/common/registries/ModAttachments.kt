package earth.terrarium.techarium.common.registries

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry
import com.teamresourceful.resourcefullibkt.common.getValue
import earth.terrarium.techarium.common.TechariumConstants
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.registries.NeoForgeRegistries

object ModAttachments {

    val registry: ResourcefulRegistry<AttachmentType<*>> =
        ResourcefulRegistries.create(NeoForgeRegistries.ATTACHMENT_TYPES, TechariumConstants.MOD_ID)

    val singleFluidTank: AttachmentType<FluidStack> by registry.register("single_fluid_tank") {
        AttachmentType.builder { _ -> FluidStack.EMPTY }
            .serialize(FluidStack.OPTIONAL_CODEC)
            .build()
    }
}