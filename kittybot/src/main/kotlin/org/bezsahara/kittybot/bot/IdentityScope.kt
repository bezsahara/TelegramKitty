package org.bezsahara.kittybot.bot

import org.bezsahara.kittybot.bot.dispatchers.AttrKey
import java.util.concurrent.atomic.AtomicInteger

class IdentityScope {
    private val attrKeyIdGen = AtomicInteger(0)

    fun highest(): Int {
        return attrKeyIdGen.get()
    }

    internal fun newAttrKeyId(): Int {
        return attrKeyIdGen.getAndAdd(1)
    }

    inline fun <reified T> attrKeyOf(name: String? = null): AttrKey<T> {
        return AttrKey(name, T::class.java, this)
    }

    fun <T> attrKeyOf(name: String? = null, clazz: Class<T>? = null): AttrKey<T> {
        return AttrKey(name, clazz, this)
    }
}
