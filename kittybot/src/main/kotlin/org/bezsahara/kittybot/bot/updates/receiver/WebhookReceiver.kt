package org.bezsahara.kittybot.bot.updates.receiver

import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.telegram.classes.updates.Update

/**
 * Updater that uses webhook. Remember that you need to set up a webhook
 */
class WebhookReceiver(
    private val updatesChannel: Channel<Update>
) : UpdateReceiver<Update> {
    override suspend fun receiveUpdates(): Update {
        return updatesChannel.receive()
    }
}