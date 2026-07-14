package org.bezsahara.kittybot.bot.builder

@Deprecated("For removal")
interface RecoverLastId {
    fun save(id: Long?)

    fun recover(): Long?
}