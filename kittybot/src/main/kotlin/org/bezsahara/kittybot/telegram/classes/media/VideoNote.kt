package org.bezsahara.kittybot.telegram.classes.media


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize


/**
 * This object represents a video message (available in Telegram apps as of v.4.0).
 * 
 * *[link](https://core.telegram.org/bots/api#videonote)*: https://core.telegram.org/bots/api#videonote
 * 
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param length Video width and height (diameter of the video message) as defined by sender
 * @param duration Duration of the video in seconds as defined by sender
 * @param thumbnail Optional. Video thumbnail
 * @param fileSize Optional. File size in bytes
 */
@Serializable
data class VideoNote(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    val length: Long,
    val duration: Long,
    val thumbnail: PhotoSize? = null,
    @SerialName("file_size") val fileSize: Long? = null
)

