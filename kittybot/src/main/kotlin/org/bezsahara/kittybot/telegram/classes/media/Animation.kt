package org.bezsahara.kittybot.telegram.classes.media


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize


/**
 * This object represents an animation file (GIF or H.264/MPEG-4 AVC video without sound).
 * 
 * *[link](https://core.telegram.org/bots/api#animation)*: https://core.telegram.org/bots/api#animation
 * 
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param width Video width as defined by sender
 * @param height Video height as defined by sender
 * @param duration Duration of the video in seconds as defined by sender
 * @param thumbnail Optional. Animation thumbnail as defined by sender
 * @param fileName Optional. Original animation filename as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Animation(
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

