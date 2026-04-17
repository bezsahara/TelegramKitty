package org.bezsahara.kittybot.telegram.classes.gifts

import org.bezsahara.kittybot.telegram.classes.gifts.Gift
import kotlinx.serialization.SerialName
import kotlin.collections.List
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * Describes a service message about a regular gift that was sent or received.
 * 
 * [link](https://core.telegram.org/bots/api#giftinfo): https://core.telegram.org/bots/api#giftinfo
 * 
 * @param gift Information about the gift
 * @param ownedGiftId Optional. Unique identifier of the received gift for the bot; only present for gifts received on behalf of business accounts
 * @param convertStarCount Optional. Number of Telegram Stars that can be claimed by the receiver by converting the gift; omitted if conversion to Telegram Stars is impossible
 * @param prepaidUpgradeStarCount Optional. Number of Telegram Stars that were prepaid for the ability to upgrade the gift
 * @param isUpgradeSeparate Optional. True, if the gift's upgrade was purchased after the gift was sent
 * @param canBeUpgraded Optional. True, if the gift can be upgraded to a unique gift
 * @param text Optional. Text of the message that was added to the gift
 * @param entities Optional. Special entities that appear in the text
 * @param isPrivate Optional. True, if the sender and gift text are shown only to the gift receiver; otherwise, everyone will be able to see them
 * @param uniqueGiftNumber Optional. Unique number reserved for this gift when upgraded. See the number field in UniqueGift
 */
@Serializable
data class GiftInfo(
    val gift: Gift,
    @SerialName("owned_gift_id") val ownedGiftId: String? = null,
    @SerialName("convert_star_count") val convertStarCount: Long? = null,
    @SerialName("prepaid_upgrade_star_count") val prepaidUpgradeStarCount: Long? = null,
    @SerialName("is_upgrade_separate") val isUpgradeSeparate: Boolean? = null,
    @SerialName("can_be_upgraded") val canBeUpgraded: Boolean? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    @SerialName("is_private") val isPrivate: Boolean? = null,
    @SerialName("unique_gift_number") val uniqueGiftNumber: Long? = null
)

