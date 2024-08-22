package org.bezsahara.kittybot.bot.updates.receiver

/**
 * @see PollingReceiver
 * @see WebhookReceiver
 */
sealed interface UpdateReceiver<T> {
    suspend fun receiveUpdates(): T
}