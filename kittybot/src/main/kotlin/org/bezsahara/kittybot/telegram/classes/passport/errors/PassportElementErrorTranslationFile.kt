package org.bezsahara.kittybot.telegram.classes.passport.errors


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an issue with one of the files that constitute the translation of a document. The error is considered resolved when the file changes.
 * 
 * *[link](https://core.telegram.org/bots/api#passportelementerrortranslationfile)*: https://core.telegram.org/bots/api#passportelementerrortranslationfile
 * 
 * @param source Error source, must be translation_file
 * @param type Type of element of the user's Telegram Passport which has the issue, one of "passport", "driver_license", "identity_card", "internal_passport", "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration"
 * @param fileHash Base64-encoded file hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorTranslationFile(
    val source: String,
    val type: String,
    @SerialName("file_hash") val fileHash: String,
    val message: String
) : PassportElementError

