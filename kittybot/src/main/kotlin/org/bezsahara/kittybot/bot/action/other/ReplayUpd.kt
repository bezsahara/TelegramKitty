package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind


fun HandlerStore.replayUpdates(
    allowedKinds: Set<UpdateKind<*>>? = null,
    replayBlock: (Update, HandlerContext) -> Update?,
) {
    addHandler(ReplayUpd(allowedKinds, replayBlock))
}


class ReplayUpd(
    override val allowedKinds: Set<UpdateKind<*>>?,
    private val replayBlock: (Update, HandlerContext) -> Update?,
) : Handler {
    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        val replay = replayBlock(update, handlerContext) ?: return Decision.Next
        handlerContext.channel.send(replay)
        return Decision.Consumed
    }
}