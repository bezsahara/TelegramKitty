package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind
import org.bezsahara.kittybot.telegram.values.UniqueGiftOrigin


/**
 * Describes a service message about a unique gift that was sent or received.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegiftinfo): https://core.telegram.org/bots/api#uniquegiftinfo
 * 
 * @param gift Information about the gift
 * @param origin Origin of the gift. Currently, either "upgrade" for gifts upgraded from regular gifts, "transfer" for gifts transferred from other users or channels, "resale" for gifts bought from other users, "gifted_upgrade" for upgrades purchased after the gift was sent, or "offer" for gifts bought or sold through gift purchase offers.
 * @param lastResaleCurrency Optional. For gifts bought from other users, the currency in which the payment for the gift was done. Currently, one of "XTR" for Telegram Stars or "TON" for TON grams.
 * @param lastResaleAmount Optional. For gifts bought from other users, the price paid for the gift in either Telegram Stars or nanograms
 * @param ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @param transferStarCount Optional. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
 * @param nextTransferDate Optional. Point in time (Unix timestamp) when the gift can be transferred. If it is in the past, then the gift can be transferred now.
 */
@Serializable
data class UniqueGiftInfo(
    val gift: UniqueGift,
    val origin: UniqueGiftOrigin,
    @SerialName("last_resale_currency") val lastResaleCurrency: CurrencyKind? = null,
    @SerialName("last_resale_amount") val lastResaleAmount: Long? = null,
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,
    @SerialName("transfer_star_count") val transferStarCount: Long? = null,
    @SerialName("next_transfer_date") val nextTransferDate: Long? = null
)

