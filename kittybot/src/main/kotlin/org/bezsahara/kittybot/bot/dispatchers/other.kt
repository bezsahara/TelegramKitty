package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.builder.FelineBuilder
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.utils.unwrap

internal data class ContextHolder(val name: String)

internal fun String.toContextHolder(): ContextHolder = ContextHolder(this)

fun FelineBuilder<*>.prepare() {
    setUpCommands()
}

internal val botCommandsKey = createTypeAwareKey<MutableList<BotCommand>>()

private fun FelineBuilder<*>.setUpCommands() {
    val data = botContext[botCommandsKey] ?: return
    init {
        val previous = getMyCommands().unwrap()
        setMyCommands(data + previous).consume()
    }
}