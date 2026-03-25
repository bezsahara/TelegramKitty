package org.bezsahara.kittybot.telegram.classes.gifts

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
 * @param totalCount Optional. The total number of the gifts of this type that can be sent; for limited gifts only
 * @param remainingCount Optional. The number of remaining gifts of this type that can be sent; for limited gifts only
 * @param publisherChat Optional. Information about the chat that published the gift
 */
@Serializable
data class Gift(
    val id: String,
    val sticker: Sticker,
    @SerialName("star_count") val starCount: Long,
    @SerialName("upgrade_star_count") val upgradeStarCount: Long? = null,
    @SerialName("total_count") val totalCount: Long? = null,
    @SerialName("remaining_count") val remainingCount: Long? = null,
    @SerialName("publisher_chat") val publisherChat: Chat? = null
)

