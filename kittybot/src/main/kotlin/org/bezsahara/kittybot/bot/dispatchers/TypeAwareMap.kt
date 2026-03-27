package org.bezsahara.kittybot.bot.dispatchers

class TypeAwareMap {
    @PublishedApi internal val map = HashMap<Any, Any>()

    operator fun <T> get(key: Any): T? {
        return map[key] as T?
    }

    operator fun <T> set(key: Any, value: T) {
        map[key] = value as Any
    }

    inline fun <T: Any> getOrPut(key: Any, value: () -> T): T {
        return map.getOrPut(key, value) as T
    }
}