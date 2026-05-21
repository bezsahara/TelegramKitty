package org.bezsahara.kittybot.bot.conv

import kotlinx.coroutines.CoroutineScope

class ConvInfo internal constructor(
    internal val runtime: ConversationRuntime
) {
    val scope: CoroutineScope
        get() = runtime.scope

}

