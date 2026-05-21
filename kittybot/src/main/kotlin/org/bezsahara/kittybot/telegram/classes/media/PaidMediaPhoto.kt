package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.Serializable


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
    override val type: String = "photo"
}

