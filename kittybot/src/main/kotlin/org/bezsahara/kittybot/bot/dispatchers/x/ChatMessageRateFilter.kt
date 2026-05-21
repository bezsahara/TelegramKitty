package org.bezsahara.kittybot.bot.dispatchers.x

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.MessageScope
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

class ChatMessageRateFilter(
    private val timePeriodMillis: Long,
    private val onExceeded: (suspend MessageScope.() -> Unit)? = null
) : Handler {
    private val map = ConcurrentHashMap<Long, Long>()
    private val cleanThreshold = maxOf(timePeriodMillis * 3, 1000 * 60 * 10)
    private var sizeThresholdDynamic = 10_000

    override val allowedKinds: Set<UpdKind> = MessageUpdate.toSet()

    private fun cleanMap() {
        val current = System.currentTimeMillis()
        map.forEach { (k, v) ->
            if (current - v > cleanThreshold) {
                map.remove(k)
            }
        }
        if (sizeThresholdDynamic - map.size < sizeThresholdDynamic / 10) {
            sizeThresholdDynamic = (min((map.size.toDouble() * 1.5), Int.MAX_VALUE.toDouble())).toInt()
        }
    }

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val chatId = update.message?.chat?.id ?: return Decision.Next
        val currentTime = System.currentTimeMillis()
        val lastTime = map.putIfAbsent(chatId, currentTime) ?: return Decision.Next
        map[chatId] = currentTime
        if (map.size > sizeThresholdDynamic) {
            cleanMap()
        }
        if ((currentTime - lastTime) <= timePeriodMillis) {
            if (onExceeded != null) {
                MessageScope(update as MessageUpdate, bot, handlerContext).onExceeded()
            }
            return Decision.Consumed
        }
        return Decision.Next
    }
}

/**
 * Limits the number of messages sent by the user. Handlers will process a message
 * if it comes at least [timePeriodMillis] ms apart. Blocks updates only of incoming text messages.
 */
fun FelineDispatcher.messageRateFilter(
    timePeriodMillis: Long,
    onExceeded: (suspend MessageScope.() -> Unit)? = null
) {
    addHandlerFirst(ChatMessageRateFilter(timePeriodMillis, onExceeded))
}