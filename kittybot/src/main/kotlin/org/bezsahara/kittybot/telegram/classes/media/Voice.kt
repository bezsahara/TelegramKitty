package org.bezsahara.kittybot.telegram.classes.media


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a voice note.
 * 
 * *[link](https://core.telegram.org/bots/api#voice)*: https://core.telegram.org/bots/api#voice
 * 
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param duration Duration of the audio in seconds as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 */
@Serializable
data class Voice(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    val duration: Long,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null
)

