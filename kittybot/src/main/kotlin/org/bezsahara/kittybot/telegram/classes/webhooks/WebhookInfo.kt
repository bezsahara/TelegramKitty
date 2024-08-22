package org.bezsahara.kittybot.telegram.classes.webhooks


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes the current status of a webhook.
 * 
 * *[link](https://core.telegram.org/bots/api#webhookinfo)*: https://core.telegram.org/bots/api#webhookinfo
 * 
 * @param url Webhook URL, may be empty if webhook is not set up
 * @param hasCustomCertificate True, if a custom certificate was provided for webhook certificate checks
 * @param pendingUpdateCount Number of updates awaiting delivery
 * @param ipAddress Optional. Currently used webhook IP address
 * @param lastErrorDate Optional. Unix time for the most recent error that happened when trying to deliver an update via webhook
 * @param lastErrorMessage Optional. Error message in human-readable format for the most recent error that happened when trying to deliver an update via webhook
 * @param lastSynchronizationErrorDate Optional. Unix time of the most recent error that happened when trying to synchronize available updates with Telegram datacenters
 * @param maxConnections Optional. The maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery
 * @param allowedUpdates Optional. A list of update types the bot is subscribed to. Defaults to all update types except chat_member
 */
@Serializable
data class WebhookInfo(
    val url: String,
    @SerialName("has_custom_certificate") val hasCustomCertificate: Boolean,
    @SerialName("pending_update_count") val pendingUpdateCount: Long,
    @SerialName("ip_address") val ipAddress: String? = null,
    @SerialName("last_error_date") val lastErrorDate: Long? = null,
    @SerialName("last_error_message") val lastErrorMessage: String? = null,
    @SerialName("last_synchronization_error_date") val lastSynchronizationErrorDate: Long? = null,
    @SerialName("max_connections") val maxConnections: Long? = null,
    @SerialName("allowed_updates") val allowedUpdates: List<String>? = null
)

