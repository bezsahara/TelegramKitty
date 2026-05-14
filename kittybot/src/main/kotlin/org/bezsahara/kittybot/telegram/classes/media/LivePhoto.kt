package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.media.PhotoSize
import kotlinx.serialization.Serializable


/**
 * This object represents a live photo.
 * 
 * [link](https://core.telegram.org/bots/api#livephoto): https://core.telegram.org/bots/api#livephoto
 * 
 * @param photo Optional. Available sizes of the corresponding static photo
 * @param fileId Identifier for the video file which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for the video file which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Video width as defined by the sender
 * @param height Video height as defined by the sender
 * @param duration Duration of the video in seconds as defined by the sender
 * @param mimeType Optional. MIME type of the file as defined by the sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class LivePhoto(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    val width: Long,
    val height: Long,
    val duration: Long,
    val photo: List<PhotoSize>? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null
)

