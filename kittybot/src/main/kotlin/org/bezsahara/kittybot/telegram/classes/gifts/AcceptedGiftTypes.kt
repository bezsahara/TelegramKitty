package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object describes the types of gifts that can be gifted to a user or a chat.
 * 
 * [link](https://core.telegram.org/bots/api#acceptedgifttypes): https://core.telegram.org/bots/api#acceptedgifttypes
 * 
 * @param unlimitedGifts True, if unlimited regular gifts are accepted
 * @param limitedGifts True, if limited regular gifts are accepted
 * @param uniqueGifts True, if unique gifts or gifts that can be upgraded to unique for free are accepted
 * @param premiumSubscription True, if a Telegram Premium subscription is accepted
 */
@Serializable
data class AcceptedGiftTypes(
    @SerialName("unlimited_gifts") val unlimitedGifts: Boolean,
    @SerialName("limited_gifts") val limitedGifts: Boolean,
    @SerialName("unique_gifts") val uniqueGifts: Boolean,
    @SerialName("premium_subscription") val premiumSubscription: Boolean
)

