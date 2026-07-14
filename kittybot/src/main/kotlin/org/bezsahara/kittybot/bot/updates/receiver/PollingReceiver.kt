package org.bezsahara.kittybot.bot.updates.receiver

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.builder.RecoverLastId
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.client.TelegramError
import org.bezsahara.kittybot.telegram.client.TelegramErrorException
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions
import org.bezsahara.kittybot.telegram.utils.throwError
import kotlin.coroutines.cancellation.CancellationException
import kotlin.random.Random

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
    var lastUpdateId: Long? = null
    @Suppress("RedundantNullableReturnType")
    @JvmField internal val timeout: Long? = timeout

    init {
        if (lastIdRecovery != null) {
            lastUpdateId = lastIdRecovery.recover()
        }
    }

    fun close() {
        lastIdRecovery?.save(lastUpdateId)
    }

    suspend fun receiveUpdates(updateChannel: Channel<Update>, pollingRecovery: PollingRecovery) = coroutineScope {
        while (true) {
            val result =
                try {
                    client.getUpdates(
                        lastUpdateId,
                        null,
                        timeout,
                        allowedUpdates,
                        ro
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (t: Exception) {
                    pollingRecovery.onMethodThrow(t)
                    continue
                }

            if (result.isSuccess) {
                pollingRecovery.reset()
                val resValue = result.value
                if (resValue.size != 0) {
                    lastUpdateId = resValue[resValue.size - 1].updateId + 1
                    for (updIdx in resValue.indices) {
                        updateChannel.send(resValue[updIdx])
                    }
                }
            } else {
                pollingRecovery.onErrorResult(result.errorOrNull()!!)
            }
        }
    }

    companion object {
        private val ro = RequestOptions(2200)
    }
}


interface PollingRecovery {
    suspend fun onMethodThrow(throwable: Exception)

    suspend fun onErrorResult(telegramError: TelegramError)

    fun reset()

    class Default : PollingRecovery {
        private var failures = 0

        override suspend fun onMethodThrow(throwable: Exception) {
            failures += 1
            delay(backoffMillis())
        }

        override suspend fun onErrorResult(telegramError: TelegramError) {
            when (telegramError.errorCode) {
                429 if telegramError.parameters?.retryAfter != null -> {
                    return delay(telegramError.parameters.retryAfter * 1000)
                }
                in 500..599 -> {
                    failures++
                    return delay(backoffMillis())
                }
                else -> {
                    throw TelegramErrorException(telegramError)
                }
            }
        }

        override fun reset() {
            failures = 0
        }

        private fun backoffMillis(): Long {
            val capped = failures.coerceAtMost(5)
            val base = 1000L * (1L shl capped) // 2s, 4s, 8s, 16s, 32s max-ish
            return minOf(base, 30_000L) + Random.nextLong(0, 500)
        }
    }
}