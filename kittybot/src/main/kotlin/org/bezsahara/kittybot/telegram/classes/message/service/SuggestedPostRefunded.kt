package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.Message
import kotlinx.serialization.Serializable


/**
 * Describes a service message about a payment refund for a suggested post.
 * 
 * [link](https://core.telegram.org/bots/api#suggestedpostrefunded): https://core.telegram.org/bots/api#suggestedpostrefunded
 * 
 * @param suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param reason Reason for the refund. Currently, one of "post_deleted" if the post was deleted within 24 hours of being posted or removed from scheduled messages without being posted, or "payment_refunded" if the payer refunded their payment.
 */
@Serializable
data class SuggestedPostRefunded(
    val reason: String,
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null
)

