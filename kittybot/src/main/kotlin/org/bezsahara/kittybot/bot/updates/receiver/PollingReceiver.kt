package org.bezsahara.kittybot.bot.updates.receiver

import io.ktor.client.plugins.*
import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.RecoverLastId
import org.bezsahara.kittybot.telegram.classes.updates.Update
import org.bezsahara.kittybot.telegram.client.TApiClient
import org.bezsahara.kittybot.telegram.utils.TResult

/**
 * Updater that uses polling.
 * If Webhook was used before, remember to use [org.bezsahara.kittybot.bot.KittyBot.deleteWebhook]
 */
class PollingReceiver(
    val client: TApiClient,
    private val timeout: Long,
    private val lastIdRecovery: RecoverLastId?
) : UpdateReceiver {
    @Volatile
    var lastUpdateId: Long? = null

    init {
        if (lastIdRecovery != null) {
            lastUpdateId = lastIdRecovery.recover()
            Runtime.getRuntime().addShutdownHook(Thread {
                lastIdRecovery.save(lastUpdateId)
            })
        }
    }

    override suspend fun receiveUpdates(updateChannel: Channel<Update>) {
        while (true) {
            val result = try {
                client.getUpdates(
                    lastUpdateId,
                    null,
                    timeout,
                    null
                )
            } catch (_: HttpRequestTimeoutException) {
                continue
            }
            if (result is TResult.Success) {
                val resValue = result.value
                if (resValue.isNotEmpty()) {
                    lastUpdateId = resValue[resValue.size - 1].updateId + 1
                }
                for (upd in result.value) {
                    updateChannel.send(upd)
                }
            } else {
                throw KittyError(result.toString())
            }
        }
    }
}