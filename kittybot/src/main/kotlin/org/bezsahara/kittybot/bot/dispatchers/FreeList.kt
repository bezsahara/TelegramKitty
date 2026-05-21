package org.bezsahara.kittybot.bot.dispatchers


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
