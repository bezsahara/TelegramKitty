package org.bezsahara.kittybot.telegram.classes.users.photos


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize


/**
 * This object represent a user's profile pictures.
 * 
 * *[link](https://core.telegram.org/bots/api#userprofilephotos)*: https://core.telegram.org/bots/api#userprofilephotos
 * 
 * @param totalCount Total number of profile pictures the target user has
 * @param photos Requested profile pictures (in up to 4 sizes each)
 */
@Serializable
data class UserProfilePhotos(
    @SerialName("total_count") val totalCount: Long,
    val photos: List<List<PhotoSize>>
)

