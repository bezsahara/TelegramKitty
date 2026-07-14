package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBotConfig


interface TypeKey<T>

open class TypeKeyImpl<T>(val name: String? = null) : TypeKey<T> {
    override fun toString(): String = "TAKeyImpl($name)"
}

fun <T> typeKeyOf(name: String? = null): TypeKey<T> {
    return TypeKeyImpl(name)
}

class TypeAwareMap {
    @PublishedApi internal val map = HashMap<TypeKey<*>, Any>()

    operator fun <T> get(key: TypeKey<T>): T? {
        return map[key] as T?
    }

    operator fun <T> set(key: TypeKey<T>, value: T) {
        map[key] = value as Any
    }

    inline fun <T: Any> getOrPut(key: TypeKey<T>, value: () -> T): T {
        return map.getOrPut(key, value) as T
    }

    lateinit var kittyBotConfig: KittyBotConfig<*>

    fun <T: Any> getOrInitInBot(key: TypeKey<T>, init: KittyBotConfig<*>.() -> T): T {
        return map.getOrPut(key) { kittyBotConfig.init() } as T
    }
}


class TypeKeyMap {
    @PublishedApi internal val map = HashMap<TypeKey<*>, Any?>()

    operator fun <T> get(key: TypeKey<T>): T? {
        return map[key] as T?
    }

    operator fun <T> set(key: TypeKey<T>, value: T) {
        map[key] = value as Any
    }

    inline fun <T> getOrPut(key: TypeKey<T>, init: () -> T): T {
        return map.getOrPut(key, init) as T
    }

    fun <T> remove(key: TypeKey<T>): T? {
        return map.remove(key) as T?
    }
}