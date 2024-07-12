package earth.terrarium.techarium.common.blocks.entities

import net.minecraft.core.BlockPos
import net.minecraft.core.component.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.MutableDataComponentHolder

abstract class ComponentBlockEntity(
    type: BlockEntityType<*>,
    pos: BlockPos,
    state: BlockState
) : BlockEntity(type, pos, state), MutableDataComponentHolder {

    private val initialComponents: DataComponentMap = DataComponentMap.builder().apply(::createDefaultComponents).build()

    abstract fun createDefaultComponents(builder: DataComponentMap.Builder)

    override fun getComponents(): DataComponentMap = this.components()

    private fun <R> editComponents(action: PatchedDataComponentMap.() -> R): R {
        val patched = components as? PatchedDataComponentMap ?: PatchedDataComponentMap(initialComponents).apply { setAll(components) }
        val value = patched.let(action)
        super.setComponents(patched)
        return value
    }

    override fun <T> set(componentType: DataComponentType<in T>, value: T?): T? =
        editComponents { set(componentType, value) }

    override fun <T> remove(type: DataComponentType<out T>): T? =
        editComponents { remove(type) }

    override fun applyComponents(patch: DataComponentPatch) =
        editComponents { applyPatch(patch) }

    override fun applyComponents(components: DataComponentMap) =
        editComponents { setAll(components) }

    override fun setComponents(components: DataComponentMap) =
        editComponents { setAll(components) }
}