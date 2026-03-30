package org.bezsahara.kittybot.bot.conv

import kotlinx.coroutines.CoroutineScope
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler

class ConvInfo internal constructor(
    internal val runtime: ConversationRuntime
) {
    val scope: CoroutineScope
        get() = runtime.scope

}

