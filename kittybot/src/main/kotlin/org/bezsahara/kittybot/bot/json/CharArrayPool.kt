package org.bezsahara.kittybot.bot.json


object CharArrayPool {
    private val map = ThreadLocal.withInitial { CharArray(MIN_ARRAY_SIZE) }!!

    @JvmStatic
    fun take(): CharArray {
        return map.get()
    }

    internal const val MIN_ARRAY_SIZE = 4096
}
