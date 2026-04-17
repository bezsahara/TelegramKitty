package org.bezsahara.kittybot.telegram.classes.gifts

import org.bezsahara.kittybot.telegram.classes.gifts.GiftBackground
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat


/**
 * This object represents a gift that can be sent by the bot.
 * 
 * [link](https://core.telegram.org/bots/api#gift): https://core.telegram.org/bots/api#gift
 * 
 * @param id Unique identifier of the gift
 * @param sticker The sticker that represents the gift
 * @param starCount The number of Telegram Stars that must be paid to send the sticker
 * @param upgradeStarCount Optional. The number of Telegram Stars that must be paid to upgrade the gift to a unique one
 * @param isPremium Optional. True, if the gift can only be purchased by Telegram Premium subscribers
 * @param hasColors Optional. True, if the gift can be used (after being upgraded) to customize a user's appearance
 * @param totalCount Optional. The total number of gifts of this type that can be sent by all users; for limited gifts only
 * @param remainingCount Optional. The number of remaining gifts of this type that can be sent by all users; for limited gifts only
 * @param personalTotalCount Optional. The total number of gifts of this type that can be sent by the bot; for limited gifts only
 * @param personalRemainingCount Optional. The number of remaining gifts of this type that can be sent by the bot; for limited gifts only
 * @param background Optional. Background of the gift
 * @param uniqueGiftVariantCount Optional. The total number of different unique gifts that can be obtained by upgrading the gift
 * @param publisherChat Optional. Information about the chat that published the gift
 */
@Serializable
data class Gift(
    val id: String,
    val sticker: Sticker,
    @SerialName("star_count") val starCount: Long,
    @SerialName("upgrade_star_count") val upgradeStarCount: Long? = null,
    @SerialName("is_premium") val isPremium: Boolean? = null,
    @SerialName("has_colors") val hasColors: Boolean? = null,
    @SerialName("total_count") val totalCount: Long? = null,
    @SerialName("remaining_count") val remainingCount: Long? = null,
    @SerialName("personal_total_count") val personalTotalCount: Long? = null,
    @SerialName("personal_remaining_count") val personalRemainingCount: Long? = null,
    val background: GiftBackground? = null,
    @SerialName("unique_gift_variant_count") val uniqueGiftVariantCount: Long? = null,
    @SerialName("publisher_chat") val publisherChat: Chat? = null
)

