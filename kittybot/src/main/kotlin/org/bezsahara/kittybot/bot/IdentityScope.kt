package org.bezsahara.kittybot.bot

import org.bezsahara.kittybot.bot.dispatchers.AttrKey
import org.bezsahara.kittybot.bot.dispatchers.TypeAwareMap
import org.bezsahara.kittybot.bot.dispatchers.TypeKeyMap
import org.bezsahara.kittybot.bot.updates.HandlerContext

class IdentityScope {
    private var attrKeyIdGen = 0
    private var closed = false

    @Synchronized
    fun close(): IdentityScope {
        closed = true
        return this
    }

    @Synchronized
    fun highest(): Int {
        return attrKeyIdGen
    }

    @Synchronized
    internal fun newAttrKeyId(): Int {
        checkClosed()
        return attrKeyIdGen++
    }

    private fun checkClosed() {
        if (closed) {
            error("The IdentityScope is already closed.")
        }
    }

    inline fun <reified T> attrKeyOf(name: String? = null): AttrKey<T> {
        return AttrKey(name, T::class.java, this)
    }

    fun <T> attrKeyOf(name: String? = null, clazz: Class<T>? = null): AttrKey<T> {
        return AttrKey(name, clazz, this)
    }

    val dynamicKey by lazy {
        attrKeyOf<TypeKeyMap>("TypeAwareMap")
    }

    fun dynamicContextMap(): HandlerContext.() -> TypeKeyMap {
        val key = dynamicKey
        return {
            var res = get(key)
            if (res == null) {
                res = TypeKeyMap()
                set(key, res)
            }
            res
        }
    }
}
