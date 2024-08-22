package org.bezsahara.kittybot.telegram.classes.passport


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a file uploaded to Telegram Passport. Currently all Telegram Passport files are in JPEG format when decrypted and don't exceed 10MB.
 * 
 * *[link](https://core.telegram.org/bots/api#passportfile)*: https://core.telegram.org/bots/api#passportfile
 * 
 * @param fileId Identifier for this file, which can be used to download or reuse the file
 * @param fileUniqueId Unique identifier for this file, which is supposed to be the same over time and for different bots. Can't be used to download or reuse the file.
 * @param fileSize File size in bytes
 * @param fileDate Unix time when the file was uploaded
 */
@Serializable
data class PassportFile(
    @SerialName("file_id") val fileId: String,
    @SerialName("file_unique_id") val fileUniqueId: String,
    @SerialName("file_size") val fileSize: Long,
    @SerialName("file_date") val fileDate: Long
)

