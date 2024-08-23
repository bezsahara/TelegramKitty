package org.bezsahara.kittybot.bot.updates

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.telegram.classes.updates.Update

internal class MultiUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    updateReceiver: UpdateReceiver?,
    parallelism: Int,
    private val channel: Channel<Update>
) : Aktualisierer(
    bot,
    CoroutineScope(Dispatchers.IO),
    botDispatchers,
    updateReceiver
) {
    init {
        require(parallelism > 0) { "You need to indicate at least 1" }
    }

    private val semaphore = Semaphore(parallelism)

    @Volatile
    private var job: Job? = null

    private suspend fun getUpdates() = coroutineScope {
        while (true) {
            val update = channel.receive()
            semaphore.withPermit {
                launch {
                    applyHandlers(update)
                }
            }
        }
    }

    override fun start() {
        job = coroutineScope.launch(Dispatchers.IO) {
            if (updateReceiver != null) {
                launch {
                    updateReceiver.receiveUpdates(channel)
                }
            }

            getUpdates()
        }
    }

    override fun stop() {
        job?.cancel()
    }
}