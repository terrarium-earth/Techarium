package earth.terrarium.techarium.common.utils

import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.common.MutableDataComponentHolder
import kotlin.reflect.KProperty

class ComponentDelegate<T : Any>(private val key: DataComponentType<T>, private val default: T) {
    operator fun getValue(thisRef: MutableDataComponentHolder, property: KProperty<*>): T = thisRef[key] ?: default
    operator fun setValue(thisRef: MutableDataComponentHolder, property: KProperty<*>, value: T) {
        thisRef[key] = value
    }
}

class ComponentDelegateWithHolder<T : Any>(
    private val holder: MutableDataComponentHolder,
    private val key: DataComponentType<T>,
    private val default: T
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): T = holder[key] ?: default
    operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        holder[key] = value
    }
}

operator fun <T : Any> DataComponentType<T>.getValue(thisRef: MutableDataComponentHolder, property: KProperty<*>): T? =
    thisRef[this]

operator fun <T : Any> DataComponentType<T>.setValue(
    thisRef: MutableDataComponentHolder,
    property: KProperty<*>,
    value: T?
) {
    thisRef[this] = value
}

fun <T : Any> DataComponentType<T>.default(default: T) = ComponentDelegate(this, default)
fun <T : Any> DataComponentType<T>.default(default: T, holder: MutableDataComponentHolder) =
    ComponentDelegateWithHolder(holder, this, default)

fun buildComponentMap(builder: DataComponentMap.Builder.() -> Unit): DataComponentMap =
    DataComponentMap.builder().apply(builder).build()