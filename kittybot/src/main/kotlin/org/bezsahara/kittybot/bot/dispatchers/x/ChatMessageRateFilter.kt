package org.bezsahara.kittybot.bot.dispatchers.x

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.FilterHandler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.telegram.classes.updates.Update
import java.util.concurrent.ConcurrentHashMap

class ChatMessageRateFilter(
    private val timePeriodMillis: Long,
    private val onExceeded: (suspend MessageScope.() -> Boolean)?
) : FilterHandler {
    private val map = ConcurrentHashMap<Long, Long>()

    override suspend fun applicable(update: Update): Boolean {
        val chatId = update.message?.from?.id ?: return false
        val currentTime = System.currentTimeMillis()
        val lastTime = map.putIfAbsent(chatId, currentTime) ?: return false
        map[chatId] = currentTime
        return currentTime - lastTime <= timePeriodMillis
    }

    override suspend fun process(update: Update, bot: KittyBot): Boolean {
        return if (onExceeded != null) {
            MessageScope(
                update.message!!,
                bot
            ).run {
                onExceeded.invoke(this)
            }
        } else {
            false
        }
    }
}

/**
 * Limits the number of messages sent by the user. Handlers will process a message
 * if it comes at least [timePeriodMillis] ms apart. Blocks updates only of incoming text messages.
 */
fun FelineDispatcher.Filters.messageRateFilter(
    timePeriodMillis: Long,
    onExceeded: (suspend MessageScope.() -> Boolean)? = null
) = addFilterAtIndex(ChatMessageRateFilter(timePeriodMillis, onExceeded))