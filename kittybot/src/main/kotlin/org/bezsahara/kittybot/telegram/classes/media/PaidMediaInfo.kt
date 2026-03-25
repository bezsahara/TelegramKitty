package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.SerialName
import kotlin.collections.List
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.PaidMedia


/**
 * Describes the paid media added to a message.
 * 
 * [link](https://core.telegram.org/bots/api#paidmediainfo): https://core.telegram.org/bots/api#paidmediainfo
 * 
 * @param starCount The number of Telegram Stars that must be paid to buy access to the media
 * @param paidMedia Information about the paid media
 */
@Serializable
data class PaidMediaInfo(
    @SerialName("star_count") val starCount: Long,
    @SerialName("paid_media") val paidMedia: List<PaidMedia>
)

