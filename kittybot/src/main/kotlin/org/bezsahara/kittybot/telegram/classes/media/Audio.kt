package org.bezsahara.kittybot.telegram.classes.media


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize


/**
 * This object represents an audio file to be treated as music by the Telegram clients.
 * 
 * *[link](https://core.telegram.org/bots/api#audio)*: https://core.telegram.org/bots/api#audio
 * 
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param duration Duration of the audio in seconds as defined by sender
 * @param performer Optional. Performer of the audio as defined by sender or by audio tags
 * @param title Optional. Title of the audio as defined by sender or by audio tags
 * @param fileName Optional. Original filename as defined by sender
 * @param mimeType Optional. MIME type of the file as defined by sender
 * @param fileSize Optional. File size in bytes. It can be bigger than 2^31 and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this value.
 * @param thumbnail Optional. Thumbnail of the album cover to which the music file belongs
 */
@Serializable
data class Audio(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    val duration: Long,
    val performer: String? = null,
    val title: String? = null,
    @SerialName("file_name") val fileName: String? = null,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("file_size") val fileSize: Long? = null,
    val thumbnail: PhotoSize? = null
)

