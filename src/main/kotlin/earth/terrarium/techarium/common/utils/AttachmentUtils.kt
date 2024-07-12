package earth.terrarium.techarium.common.utils

import net.neoforged.neoforge.attachment.AttachmentHolder
import net.neoforged.neoforge.attachment.AttachmentType
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

operator fun <T : Any> AttachmentHolder.get(key: AttachmentType<T>): T = this.getData(key)
operator fun <T : Any> AttachmentHolder.set(key: AttachmentType<T>, value: T) = this.setData(key, value)
operator fun <T : Any> AttachmentHolder.minusAssign(key: AttachmentType<T>) {
    this.removeData(key)
}

operator fun <T : Any> AttachmentType<T>.getValue(thisRef: AttachmentHolder, property: KProperty<*>): T = thisRef[this]
operator fun <T : Any> AttachmentType<T>.setValue(thisRef: AttachmentHolder, property: KProperty<*>, value: T) {
    thisRef[this] = value
}

class OptionalAttachmentDelegate<T : Any>(private val key: AttachmentType<T>) :
    ReadWriteProperty<AttachmentHolder, T?> {
    override operator fun getValue(thisRef: AttachmentHolder, property: KProperty<*>): T? =
        thisRef.getExistingData(key).orElse(null)

    override operator fun setValue(thisRef: AttachmentHolder, property: KProperty<*>, value: T?) {
        if (value == null) {
            thisRef -= key
        } else {
            thisRef[key] = value
        }
    }
}

fun <T : Any> AttachmentType<T>.optional() = OptionalAttachmentDelegate(this)