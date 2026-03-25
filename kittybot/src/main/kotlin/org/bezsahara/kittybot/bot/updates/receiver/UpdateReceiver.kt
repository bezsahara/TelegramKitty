package org.bezsahara.kittybot.bot.updates.receiver

import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.telegram.classes.core.update.Update

/**
 * @see PollingReceiver
 * @see WebhookReceiver
 */
sealed interface UpdateReceiver {
    suspend fun receiveUpdates(updateChannel: Channel<Update>)
}