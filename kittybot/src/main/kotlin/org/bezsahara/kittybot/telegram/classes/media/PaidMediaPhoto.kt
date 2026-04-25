package org.bezsahara.kittybot.telegram.classes.media

import org.bezsahara.kittybot.telegram.values.PaidMediaType
import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.media.PhotoSize
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.PaidMedia


/**
 * The paid media is a photo.
 * 
 * [link](https://core.telegram.org/bots/api#paidmediaphoto): https://core.telegram.org/bots/api#paidmediaphoto
 * 
 * @param type Type of the paid media, always "photo"
 * @param photo The photo
 */
@Serializable
data class PaidMediaPhoto(
    val photo: List<PhotoSize>
) : PaidMedia {
    override val type: PaidMediaType = PaidMediaType.PHOTO
}

