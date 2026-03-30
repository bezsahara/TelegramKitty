package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.KittyBotConfig


interface TAKey<T>

open class TAKeyImpl<T>(val name: String? = null) : TAKey<T> {
    override fun toString(): String = "TAKeyImpl($name)"
}

fun <T> createTypeAwareKey(name: String? = null): TAKey<T> {
    return TAKeyImpl(name)
}

class TypeAwareMap {
    @PublishedApi internal val map = HashMap<TAKey<*>, Any>()

    operator fun <T> get(key: TAKey<T>): T? {
        return map[key] as T?
    }

    operator fun <T> set(key: TAKey<T>, value: T) {
        map[key] = value as Any
    }

    inline fun <T: Any> getOrPut(key: TAKey<T>, value: () -> T): T {
        return map.getOrPut(key, value) as T
    }

    lateinit var kittyBotConfig: KittyBotConfig<*>

    fun <T: Any> getOrInitInBot(key: TAKey<T>, init: KittyBotConfig<*>.() -> T): T {
        return map.getOrPut(key) { kittyBotConfig.init() } as T
    }
}