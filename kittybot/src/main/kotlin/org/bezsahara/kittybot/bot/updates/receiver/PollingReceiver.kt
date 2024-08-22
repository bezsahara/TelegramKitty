package org.bezsahara.kittybot.bot.updates.receiver

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
) : UpdateReceiver<TResult<List<Update>>> {
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

    override suspend fun receiveUpdates(): TResult<List<Update>> {
        val result = client.getUpdates(
            lastUpdateId,
            null,
            timeout,
            null
        )
        if (result is TResult.Success) {
            val resValue = result.value
            if (resValue.isNotEmpty()) {
                lastUpdateId = resValue[resValue.size - 1].updateId + 1
            }
        }
        return result
    }
}