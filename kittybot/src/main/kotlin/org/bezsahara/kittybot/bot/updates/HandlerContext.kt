package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.bot.IdentityScope
import org.bezsahara.kittybot.bot.dispatchers.AttrKey
import org.bezsahara.kittybot.bot.errors.KittyError


sealed class HandlerContext {
    abstract operator fun <T> get(a: AttrKey<T>): T?

    abstract operator fun <T> set(attrKey: AttrKey<T>, value: T)

    protected fun throwWrongScope(used: AttrKey<*>) {
        throw KittyError("AttrKey of $used was created with different identity scope!")
    }
}

class HandlerContextMap(size: Int, private val identityScope: IdentityScope) : HandlerContext() {
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

class HandlerContextArray(size: Int, private val identityScope: IdentityScope) : HandlerContext() {
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
    abstract fun create(size: Int, identityScope: IdentityScope): HandlerContext

    class ByMap : HandlerContextBuilder() {
        override fun create(size: Int, identityScope: IdentityScope): HandlerContext {
            return HandlerContextMap(size, identityScope)
        }
    }

    class ByArray : HandlerContextBuilder() {
        override fun create(size: Int, identityScope: IdentityScope): HandlerContext {
            return HandlerContextArray(size, identityScope)
        }
    }
}