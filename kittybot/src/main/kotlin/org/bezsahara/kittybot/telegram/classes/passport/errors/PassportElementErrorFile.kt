package org.bezsahara.kittybot.telegram.classes.passport.errors


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an issue with a document scan. The error is considered resolved when the file with the document scan changes.
 * 
 * *[link](https://core.telegram.org/bots/api#passportelementerrorfile)*: https://core.telegram.org/bots/api#passportelementerrorfile
 * 
 * @param source Error source, must be file
 * @param type The section of the user's Telegram Passport which has the issue, one of "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration"
 * @param fileHash Base64-encoded file hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorFile(
    val source: String,
    val type: String,
    @SerialName("file_hash") val fileHash: String,
    val message: String
) : PassportElementError

