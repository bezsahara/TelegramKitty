package org.bezsahara.kittybot.bot.action.other

inline fun <T> MutableList<T>.replaceLast(block: (T) -> T): T {
    val lastOneNew = block(this[size-1])
    this[size-1] = lastOneNew
    return lastOneNew
}