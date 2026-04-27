package org.bezsahara.kittybot.telegram.classes.gifts

import org.bezsahara.kittybot.telegram.classes.gifts.Gift
import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.gifts.OwnedGift


/**
 * Describes a regular gift owned by a user or a chat.
 * 
 * [link](https://core.telegram.org/bots/api#ownedgiftregular): https://core.telegram.org/bots/api#ownedgiftregular
 * 
 * @param type Type of the gift, always "regular"
 * @param gift Information about the regular gift
 * @param ownedGiftId Optional. Unique identifier of the gift for the bot; for gifts received on behalf of business accounts only
 * @param senderUser Optional. Sender of the gift if it is a known user
 * @param sendDate Date the gift was sent in Unix time
 * @param text Optional. Text of the message that was added to the gift
 * @param entities Optional. Special entities that appear in the text
 * @param isPrivate Optional. True, if the sender and gift text are shown only to the gift receiver; otherwise, everyone will be able to see them
 * @param isSaved Optional. True, if the gift is displayed on the account's profile page; for gifts received on behalf of business accounts only
 * @param canBeUpgraded Optional. True, if the gift can be upgraded to a unique gift; for gifts received on behalf of business accounts only
 * @param wasRefunded Optional. True, if the gift was refunded and isn't available anymore
 * @param convertStarCount Optional. Number of Telegram Stars that can be claimed by the receiver instead of the gift; omitted if the gift cannot be converted to Telegram Stars; for gifts received on behalf of business accounts only
 * @param prepaidUpgradeStarCount Optional. Number of Telegram Stars that were paid for the ability to upgrade the gift
 * @param isUpgradeSeparate Optional. True, if the gift's upgrade was purchased after the gift was sent; for gifts received on behalf of business accounts only
 * @param uniqueGiftNumber Optional. Unique number reserved for this gift when upgraded. See the number field in UniqueGift
 */
@Serializable
data class OwnedGiftRegular(
    val gift: Gift,
    @SerialName("send_date") val sendDate: Long,
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,
    @SerialName("sender_user") val senderUser: User? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    @SerialName("is_private") val isPrivate: Boolean? = null,
    @SerialName("is_saved") val isSaved: Boolean? = null,
    @SerialName("can_be_upgraded") val canBeUpgraded: Boolean? = null,
    @SerialName("was_refunded") val wasRefunded: Boolean? = null,
    @SerialName("convert_star_count") val convertStarCount: Long? = null,
    @SerialName("prepaid_upgrade_star_count") val prepaidUpgradeStarCount: Long? = null,
    @SerialName("is_upgrade_separate") val isUpgradeSeparate: Boolean? = null,
    @SerialName("unique_gift_number") val uniqueGiftNumber: Long? = null
) : OwnedGift {
    override val type: String = "regular"
}

