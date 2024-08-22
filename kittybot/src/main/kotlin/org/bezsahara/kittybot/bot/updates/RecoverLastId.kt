package org.bezsahara.kittybot.bot.updates

interface RecoverLastId {
    fun save(id: Long?)

    fun recover(): Long?
}
