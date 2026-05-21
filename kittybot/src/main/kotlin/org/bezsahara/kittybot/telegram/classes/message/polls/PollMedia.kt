package org.bezsahara.kittybot.telegram.classes.message.polls

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.*
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import org.bezsahara.kittybot.telegram.classes.media.geo.Venue
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker


/**
 * At most one of the optional fields can be present in any given object.
 * 
 * [link](https://core.telegram.org/bots/api#pollmedia): https://core.telegram.org/bots/api#pollmedia
 * 
 * @param animation Optional. Media is an animation, information about the animation
 * @param audio Optional. Media is an audio file, information about the file; currently, can't be received in a poll option
 * @param document Optional. Media is a general file, information about the file; currently, can't be received in a poll option
 * @param livePhoto Optional. Media is a live photo, information about the live photo
 * @param location Optional. Media is a shared location, information about the location
 * @param photo Optional. Media is a photo, available sizes of the photo
 * @param sticker Optional. Media is a sticker, information about the sticker; currently, for poll options only
 * @param venue Optional. Media is a venue, information about the venue
 * @param video Optional. Media is a video, information about the video
 */
@Serializable
data class PollMedia(
    val animation: Animation? = null,
    val audio: Audio? = null,
    val document: Document? = null,
    @SerialName("live_photo") val livePhoto: LivePhoto? = null,
    val location: Location? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val venue: Venue? = null,
    val video: Video? = null
)

