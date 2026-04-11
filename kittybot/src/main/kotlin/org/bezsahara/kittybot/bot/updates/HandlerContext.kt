package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ChannelResult
import org.bezsahara.kittybot.bot.IdentityScope
import org.bezsahara.kittybot.bot.dispatchers.AttrKey
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.telegram.classes.core.update.SyntheticUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.Update


sealed class HandlerContext(internal val channel: Channel<Update>) {
    abstract operator fun <T> get(a: AttrKey<T>): T?

    abstract operator fun <T> set(attrKey: AttrKey<T>, value: T)

    suspend fun emitUpdate(u: SyntheticUpdate) {
        channel.send(u)
    }

    fun tryEmitUpdate(u: SyntheticUpdate): ChannelResult<Unit> {
        return channel.trySend(u)
    }

    protected fun throwWrongScope(used: AttrKey<*>) {
        throw KittyError("AttrKey of $used was created with different identity scope!")
    }
}

class HandlerContextMap(size: Int, private val identityScope: IdentityScope, channel: Channel<Update>) : HandlerContext(channel) {
    private val map = HashMap<Int, Any>(size, 1f)
    private val size = size

    override operator fun <T> get(a: AttrKey<T>): T? {
        if (a.scope !== identityScope) throwWrongScope(a)
        val id = a.id
        if (id < size) {
            return map[id] as T
        } else {
            error("You did not register AttrKey $a, you can do it in FelineDispatcher")
        }
    }

    override operator fun <T> set(attrKey: AttrKey<T>, value: T) {
        if (attrKey.scope !== identityScope) throwWrongScope(attrKey)
        val id = attrKey.id
        if (id < size) {
            map[id] = value as Any
        } else {
            error("You did not register AttrKey $attrKey, you can do it in FelineDispatcher")
        }
    }
}

class HandlerContextArray(size: Int, private val identityScope: IdentityScope, channel: Channel<Update>) : HandlerContext(channel) {
    private val map = arrayOfNulls<Any>(size)
    private val size = size

    override operator fun <T> get(a: AttrKey<T>): T? {
        if (a.scope !== identityScope) throwWrongScope(a)
        val id = a.id
        if (id < size) {
            return map[id] as T
        } else {
            error("You did not register AttrKey $a, you can do it in FelineDispatcher")
        }
    }

    override operator fun <T> set(attrKey: AttrKey<T>, value: T) {
        if (attrKey.scope !== identityScope) throwWrongScope(attrKey)
        val id = attrKey.id
        if (id < size) {
            map[id] = value
        } else {
            error("You did not register AttrKey $attrKey, you can do it in FelineDispatcher")
        }
    }
}

sealed class HandlerContextBuilder {
    abstract fun create(size: Int, identityScope: IdentityScope, channel: Channel<Update>): HandlerContext

    class ByMap : HandlerContextBuilder() {
        override fun create(size: Int, identityScope: IdentityScope, channel: Channel<Update>): HandlerContext {
            return HandlerContextMap(size, identityScope, channel)
        }
    }

    class ByArray : HandlerContextBuilder() {
        override fun create(size: Int, identityScope: IdentityScope, channel: Channel<Update>): HandlerContext {
            return HandlerContextArray(size, identityScope, channel)
        }
    }
}