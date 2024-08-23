package org.bezsahara.kittybot.bot.updates.receiver

import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.telegram.classes.updates.Update

/**
 * Updater that uses webhook. Remember that you need to set up a webhook.
 *
 * @see org.bezsahara.kittybot.bot.KittyBot.setWebhook
 * @see org.bezsahara.kittybot.bot.onUpdate
 */
data object WebhookReceiver : UpdateReceiver {
    override suspend fun receiveUpdates(updateChannel: Channel<Update>) = Unit
}
