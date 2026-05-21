package org.bezsahara.kittybot.telegram.classes.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.Audio


/**
 * This object represents the audios displayed on a user's profile.
 * 
 * [link](https://core.telegram.org/bots/api#userprofileaudios): https://core.telegram.org/bots/api#userprofileaudios
 * 
 * @param totalCount Total number of profile audios for the target user
 * @param audios Requested profile audios
 */
@Serializable
data class UserProfileAudios(
    @SerialName("total_count") val totalCount: Long,
    val audios: List<Audio>
)

