package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.payments.StarAmount


/**
 * Describes a service message about a successful payment for a suggested post.
 * 
 * [link](https://core.telegram.org/bots/api#suggestedpostpaid): https://core.telegram.org/bots/api#suggestedpostpaid
 * 
 * @param suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param currency Currency in which the payment was made. Currently, one of "XTR" for Telegram Stars or "TON" for toncoins.
 * @param amount Optional. The amount of the currency that was received by the channel in nanotoncoins; for payments in toncoins only
 * @param starAmount Optional. The amount of Telegram Stars that was received by the channel; for payments in Telegram Stars only
 */
@Serializable
data class SuggestedPostPaid(
    val currency: CurrencyKind,
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null,
    val amount: Long? = null,
    @SerialName("star_amount") val starAmount: StarAmount? = null
)

