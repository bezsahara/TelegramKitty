package org.bezsahara.kittybot.telegram.classes.media


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize


/**
 * This object represents a video file.
 * 
 * *[link](https://core.telegram.org/bots/api#video)*: https://core.telegram.org/bots/api#video
 * 
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Video width as defined by sender
 * @param height Video height as defined by sender
 * @param duration Duration of the video in seconds as defined by sender
 * @param thumbnail Optional. Video thumbnail
 * @param fileName Optional. Original filename as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Video(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    val width: Long,
    val height: Long,
    val duration: Long,
    val thumbnail: PhotoSize? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null
)

