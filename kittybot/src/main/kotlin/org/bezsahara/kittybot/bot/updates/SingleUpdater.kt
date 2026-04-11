package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.telegram.classes.core.update.Update

@OptIn(ExperimentalCoroutinesApi::class)
internal class SingleUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    private val channel: Channel<Update>,
    private val coroutineScope: CoroutineScope,
    errorHandler: HandlerErrorHandler,
    furballConfig: FurballConfig
) : Furball(
    bot,
    botDispatchers,
    errorHandler,
    furballConfig,
    channel
) {

    private suspend fun getUpdates() = coroutineScope {
        while (isActive) {
            applyHandlers(channel.receive())
        }
    }

    override fun start() {
        coroutineScope.launch {
            getUpdates()
        }
    }
}