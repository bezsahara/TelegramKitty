package org.bezsahara.kittybot.bot

import org.bezsahara.kittybot.bot.dispatchers.AttrKey
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

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
}
