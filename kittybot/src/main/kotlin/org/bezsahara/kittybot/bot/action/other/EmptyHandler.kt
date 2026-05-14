package org.bezsahara.kittybot.bot.action.other

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentityDelegate
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

class EmptyHandler(private val returns: Decision, override val allowedKinds: Set<UpdateKind<*>>? = null) : Handler {
    constructor() : this(Decision.Next)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext,
    ): Decision {
        return returns
    }

    override val identity: HandlerIdentity by HandlerIdentityDelegate()
}