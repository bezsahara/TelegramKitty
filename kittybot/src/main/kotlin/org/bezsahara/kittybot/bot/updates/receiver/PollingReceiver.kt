package org.bezsahara.kittybot.bot.updates.receiver

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.builder.RecoverLastId
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions
import org.bezsahara.kittybot.telegram.utils.throwError

/**
 * Updater that uses polling.
 * If Webhook was used before, remember to use [org.bezsahara.kittybot.bot.KittyBot.deleteWebhook]
 */
class PollingReceiver(
    val client: KittyBot,
    timeout: Long,
    private val lastIdRecovery: RecoverLastId?,
    val allowedUpdates: List<String>?
) : UpdateReceiver {
    @JvmField
    @Volatile  // TODO remove it.
    var lastUpdateId: Long? = null
    @JvmField internal val timeout = java.lang.Long.valueOf(timeout)

    init {
        if (lastIdRecovery != null) {
            lastUpdateId = lastIdRecovery.recover()
        }
    }

    fun close() {
        lastIdRecovery?.save(lastUpdateId)
    }

    override suspend fun receiveUpdates(updateChannel: Channel<Update>) = coroutineScope {
        while (isActive) {
            val result =
                client.getUpdates(
                    lastUpdateId,
                    null,
                    timeout,
                    allowedUpdates,
                    ro
                )


            if (result.isSuccess) {
                val resValue = result.value
                if (resValue.isNotEmpty()) {
                    lastUpdateId = resValue[resValue.size - 1].updateId + 1
                }
                for (updIdx in resValue.indices) {
                    updateChannel.send(resValue[updIdx])
                }
            } else {
                result.throwError()
            }
        }
    }

    companion object {
        private val ro = RequestOptions(2200)
    }
}