package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGift
import kotlinx.serialization.Serializable


/**
 * Describes a service message about a unique gift that was sent or received.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegiftinfo): https://core.telegram.org/bots/api#uniquegiftinfo
 * 
 * @param gift Information about the gift
 * @param origin Origin of the gift. Currently, either "upgrade" for gifts upgraded from regular gifts, "transfer" for gifts transferred from other users or channels, or "resale" for gifts bought from other users
 * @param lastResaleStarCount Optional. For gifts bought from other users, the price paid for the gift
 * @param ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @param transferStarCount Optional. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
 * @param nextTransferDate Optional. Point in time (Unix timestamp) when the gift can be transferred. If it is in the past, then the gift can be transferred now
 */
@Serializable
data class UniqueGiftInfo(
    val gift: UniqueGift,
    val origin: String,
    @SerialName("last_resale_star_count") val lastResaleStarCount: Long? = null,
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,
    @SerialName("transfer_star_count") val transferStarCount: Long? = null,
    @SerialName("next_transfer_date") val nextTransferDate: Long? = null
)

