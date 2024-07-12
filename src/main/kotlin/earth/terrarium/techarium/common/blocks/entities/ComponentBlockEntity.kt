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

    private fun getPatchedComponents(): PatchedDataComponentMap =
        components as? PatchedDataComponentMap ?: PatchedDataComponentMap(initialComponents).apply { setAll(components) }

    override fun getComponents(): DataComponentMap = this.components()

    override fun <T : Any?> set(componentType: DataComponentType<in T>, value: T?): T? {
        val patched = getPatchedComponents()
        val oldValue = patched.set(componentType, value)
        super.setComponents(patched)
        return oldValue
    }

    override fun <T : Any?> remove(type: DataComponentType<out T>): T? {
        val patched = getPatchedComponents()
        val value = patched.remove(type)
        super.setComponents(patched)
        return value
    }

    override fun applyComponents(patch: DataComponentPatch) {
        super.setComponents(getPatchedComponents().apply { applyPatch(patch) })
    }

    override fun applyComponents(components: DataComponentMap) {
        super.setComponents(getPatchedComponents().apply { setAll(components) })
    }

    override fun setComponents(components: DataComponentMap) {
        super.setComponents(getPatchedComponents().apply { setAll(components) })
    }
}