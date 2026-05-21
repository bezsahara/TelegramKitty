package org.bezsahara.kittybot.bot.action.each

import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.HandlerCheck
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update


data class UpdateAndContext(val update: Update, val handlerContext: HandlerContext)

class ChannelConsumption(
    private val handlerCheck: HandlerCheck,
    override val allowedKinds: Set<UpdKind>?,
    private val channel: Channel<UpdateAndContext>
) : Handler {
    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        if (!handlerCheck.check(update, handlerContext)) return Decision.Next

        channel.send(UpdateAndContext(update, handlerContext))

        return Decision.Consumed
    }
}

fun HandlerStore.consumeUpdatesInChannel(
    handlerCheck: HandlerCheck,
    allowedKinds: Set<UpdKind>? = null,
    channel: Channel<UpdateAndContext> = Channel(1024)
): Channel<UpdateAndContext> {
    addHandler(ChannelConsumption(handlerCheck, allowedKinds, channel))
    return channel
}
