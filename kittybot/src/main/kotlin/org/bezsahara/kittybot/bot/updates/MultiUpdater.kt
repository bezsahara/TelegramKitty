package org.bezsahara.kittybot.bot.updates

import io.ktor.client.plugins.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.telegram.utils.TResult

internal class MultiUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    updateReceiver: UpdateReceiver<*>,
    parallelism: Int,
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
        when (updateReceiver) {

            is PollingReceiver -> while (true) {
                try {
                    when (val updates = updateReceiver.receiveUpdates()) {
                        is TResult.Failure -> throw KittyError(updates.cause.toString())
                        is TResult.Success -> updates.value.forEach {
                            semaphore.withPermit {
                                launch {
                                    applyHandlers(it)
                                }
                            }
                        }
                    }
                } catch (_: HttpRequestTimeoutException) {
                    println("Timeout!")
                }
                yield()
            }

            is WebhookReceiver -> while (true) {
                val newUpdate = updateReceiver.receiveUpdates()
                semaphore.withPermit {
                    launch {
                        applyHandlers(newUpdate)
                    }
                }
            }

        }
    }

    override fun start() {
        job = coroutineScope.launch(Dispatchers.IO) {
            getUpdates()
        }
    }

    override fun stop() {
        job?.cancel()
    }
}