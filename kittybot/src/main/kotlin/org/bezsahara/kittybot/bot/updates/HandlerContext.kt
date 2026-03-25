package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.bot.dispatchers.AttrKey


sealed class HandlerContext {
    abstract operator fun <T> get(a: AttrKey<T>): T?

    abstract operator fun <T> set(attrKey: AttrKey<T>, value: T)
}

class HandlerContextMap(size: Int) : HandlerContext() {
    private val map = HashMap<Int, Any>(size, 1f)
    private val size = size

    override operator fun <T> get(a: AttrKey<T>): T? {
        val id = a.id
        if (id < size) {
            return map[id] as T
        } else {
            error("You did not register AttrKey $a, you can do it in FelineDispatcher")
        }
    }

    override operator fun <T> set(attrKey: AttrKey<T>, value: T) {
        val id = attrKey.id
        if (id < size) {
            map[id] = value as Any
        } else {
            error("You did not register AttrKey $attrKey, you can do it in FelineDispatcher")
        }
    }
}

class HandlerContextArray(size: Int) : HandlerContext() {
    private val map = arrayOfNulls<Any>(size)
    private val size = size

    override operator fun <T> get(a: AttrKey<T>): T? {
        val id = a.id
        if (id < size) {
            return map[id] as T
        } else {
            error("You did not register AttrKey $a, you can do it in FelineDispatcher")
        }
    }

    override operator fun <T> set(attrKey: AttrKey<T>, value: T) {
        val id = attrKey.id
        if (id < size) {
            map[id] = value
        } else {
            error("You did not register AttrKey $attrKey, you can do it in FelineDispatcher")
        }
    }
}

sealed class HandlerContextBuilder {
    abstract fun create(size: Int): HandlerContext

    class ByMap : HandlerContextBuilder() {
        override fun create(size: Int): HandlerContext {
            return HandlerContextMap(size)
        }
    }

    class ByArray : HandlerContextBuilder() {
        override fun create(size: Int): HandlerContext {
            return HandlerContextArray(size)
        }
    }
}