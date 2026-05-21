package org.bezsahara.kittybot.bot.updates.updaters

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.updates.furballs.Furball
import org.bezsahara.kittybot.telegram.classes.core.update.Update

@OptIn(ExperimentalCoroutinesApi::class)
internal class SingleUpdater(
    private val channel: Channel<Update>,
    private val coroutineScope: CoroutineScope,
    private val furball: Furball
) : Updater {

    private suspend fun getUpdates() = coroutineScope {
        while (isActive) {
            furball.applyHandlers(channel.receive())
        }
    }

    override fun start() {
        coroutineScope.launch {
            getUpdates()
        }
    }
}