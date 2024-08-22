package org.bezsahara.kittybot.telegram.classes.passport.errors


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an issue with a list of scans. The error is considered resolved when the list of files containing the scans changes.
 * 
 * *[link](https://core.telegram.org/bots/api#passportelementerrorfiles)*: https://core.telegram.org/bots/api#passportelementerrorfiles
 * 
 * @param source Error source, must be files
 * @param type The section of the user's Telegram Passport which has the issue, one of "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration"
 * @param fileHashes List of base64-encoded file hashes
 * @param message Error message
 */
@Serializable
data class PassportElementErrorFiles(
    val source: String,
    val type: String,
    @SerialName("file_hashes") val fileHashes: List<String>,
    val message: String
) : PassportElementError

