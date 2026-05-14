package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGift
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.gifts.OwnedGift


/**
 * Describes a unique gift received and owned by a user or a chat.
 * 
 * [link](https://core.telegram.org/bots/api#ownedgiftunique): https://core.telegram.org/bots/api#ownedgiftunique
 * 
 * @param type Type of the gift, always "unique"
 * @param gift Information about the unique gift
 * @param ownedGiftId Optional. Unique identifier of the received gift for the bot; for gifts received on behalf of business accounts only
 * @param senderUser Optional. Sender of the gift if it is a known user
 * @param sendDate Date the gift was sent in Unix time
 * @param isSaved Optional. True, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
 * @param canBeTransferred Optional. True, if the gift can be transferred to another owner; for gifts received on behalf of business accounts only
 * @param transferStarCount Optional. Number of Telegram Stars that must be paid to transfer the gift; omitted if the bot cannot transfer the gift
 * @param nextTransferDate Optional. Point in time (Unix timestamp) when the gift can be transferred. If it is in the past, then the gift can be transferred now.
 */
@Serializable
data class OwnedGiftUnique(
    val gift: UniqueGift,
    @SerialName("send_date") val sendDate: Long,
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,
    @SerialName("sender_user") val senderUser: User? = null,
    @SerialName("is_saved") val isSaved: Boolean? = null,
    @SerialName("can_be_transferred") val canBeTransferred: Boolean? = null,
    @SerialName("transfer_star_count") val transferStarCount: Long? = null,
    @SerialName("next_transfer_date") val nextTransferDate: Long? = null
) : OwnedGift {
    override val type: String = "unique"
}

