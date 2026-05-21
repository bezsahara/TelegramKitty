package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * The paid media is a live photo.
 * 
 * [link](https://core.telegram.org/bots/api#paidmedialivephoto): https://core.telegram.org/bots/api#paidmedialivephoto
 * 
 * @param type Type of the paid media, always "live_photo"
 * @param livePhoto The photo
 */
@Serializable
data class PaidMediaLivePhoto(
    @SerialName("live_photo") val livePhoto: LivePhoto
) : PaidMedia {
    override val type: String = "live_photo"
}

