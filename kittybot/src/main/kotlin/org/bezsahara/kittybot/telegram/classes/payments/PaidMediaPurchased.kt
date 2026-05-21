package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * This object contains information about a paid media purchase.
 * 
 * [link](https://core.telegram.org/bots/api#paidmediapurchased): https://core.telegram.org/bots/api#paidmediapurchased
 * 
 * @param from User who purchased the media
 * @param paidMediaPayload Bot-specified paid media payload
 */
@Serializable
data class PaidMediaPurchased(
    val from: User,
    @SerialName("paid_media_payload") val paidMediaPayload: String
)

