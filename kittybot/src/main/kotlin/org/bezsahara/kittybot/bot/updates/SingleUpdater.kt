package org.bezsahara.kittybot.bot.updates

import io.ktor.client.plugins.*
import kotlinx.coroutines.*
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.receiver.UpdateReceiver
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver
import org.bezsahara.kittybot.telegram.utils.TResult
import java.util.concurrent.Executors

internal class SingleUpdater(
    bot: KittyBot,
    botDispatchers: FelineDispatcher,
    updateReceiver: UpdateReceiver<*>,
    private val coroutineDispatcher: ExecutorCoroutineDispatcher =
        Executors
            .newSingleThreadExecutor()
            .asCoroutineDispatcher()
) : Aktualisierer(
    bot,
    CoroutineScope(coroutineDispatcher),
    botDispatchers,
    updateReceiver
) {
    @Volatile
    var job: Job? = null

    private suspend fun getUpdates() = when (updateReceiver) {
        is PollingReceiver -> while (true) {
            try {
                when (val updates = updateReceiver.receiveUpdates()) {
                    is TResult.Failure -> throw KittyError(updates.toString())
                    is TResult.Success -> updates.value.forEach {
                        applyHandlers(it)
                    }
                }
            } catch (_: HttpRequestTimeoutException) { }
        }

        is WebhookReceiver -> while (true) {
            applyHandlers(updateReceiver.receiveUpdates())
        }

    }

    override fun start() {
        job = coroutineScope.launch {
            getUpdates()
        }
    }

    override fun stop() {
        job?.cancel()
        coroutineDispatcher.close()
        coroutineScope.cancel()
    }
}