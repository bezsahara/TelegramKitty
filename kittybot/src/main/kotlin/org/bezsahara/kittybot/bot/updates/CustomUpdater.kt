package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.telegram.classes.core.update.Update

fun interface CustomUpdaterSetup {
    // this function is called once, return immediately.
    // Attach supervisorJob to your CoroutineContext.
    fun configure(channel: Channel<Update>, applyHandlers: suspend (Update) -> Unit, supervisorJob: CompletableJob)
}

internal class CustomUpdater(
    bot: KittyBot,
    private val customUpdater: CustomUpdaterSetup,
    felineDispatcher: FelineDispatcher,
    val channel: Channel<Update>,
    private val supervisorJob: CompletableJob,
    errorHandler: HandlerErrorHandler,
    furballConfig: FurballConfig
) : Furball(
    bot,
    felineDispatcher,
    errorHandler,
    furballConfig
) {
    override fun start() {
        customUpdater.configure(channel, ::applyHandlers, supervisorJob)
    }
}