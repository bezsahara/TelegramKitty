package org.bezsahara.kittybot.bot.builder

interface RecoverLastId {
    fun save(id: Long?)

    fun recover(): Long?
}