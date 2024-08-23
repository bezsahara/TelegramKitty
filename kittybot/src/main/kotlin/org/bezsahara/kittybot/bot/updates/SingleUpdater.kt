package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.telegram.classes.updates.Update
import java.util.concurrent.Executors

internal class SingleUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    updateReceiver: UpdateReceiver?,
    private val channel: Channel<Update>,
    private val coroutineDispatcher: ExecutorCoroutineDispatcher =
        Executors
            .newSingleThreadExecutor()
            .asCoroutineDispatcher(),
) : Aktualisierer(
    bot,
    CoroutineScope(coroutineDispatcher),
    botDispatchers,
    updateReceiver
) {
    @Volatile
    var job: Job? = null

    private suspend fun getUpdates() {
        while (true) {
            applyHandlers(channel.receive())
        }
    }

    override fun start() {
        job = coroutineScope.launch {
            if (updateReceiver != null) {
                launch(Dispatchers.IO) {
                    updateReceiver.receiveUpdates(channel)
                }
            }
            getUpdates()
        }
    }

    override fun stop() {
        job?.cancel()
        coroutineDispatcher.close()
        coroutineScope.cancel()
    }
}