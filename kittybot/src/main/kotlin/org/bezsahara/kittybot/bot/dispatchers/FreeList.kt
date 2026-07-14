package org.bezsahara.kittybot.bot.dispatchers

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class FreeRef<T>(
    ref: T
) {
    private object Empty
    private var ref: Any? = ref

    fun get(): T {
        val r = ref
        if (r === Empty) error("Reference was already freed")
        return r as T
    }

    fun free() {
        ref = Empty
    }
}

class ReferencesContainer() {
    private var container: ArrayList<RefProperty<*>>? = arrayListOf()

    fun releaseAll() {
        val container = container ?: return
        container.forEach {
            it.free()
        }
        this.container = null
    }

    fun <T> capture(ref: T): RefProperty<T> {
        val p = RefProperty(ref)
        container?.add(p) ?: error("ReferencesContainer was released already!")
        return p
    }
}


class RefProperty<V>(
    ref: V
) : ReadWriteProperty<Any?, V> {
    private object Empty
    private var ref: Any? = ref

    override fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return ref as V
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        ref = value
    }

    fun free() {
        ref = Empty
    }
}