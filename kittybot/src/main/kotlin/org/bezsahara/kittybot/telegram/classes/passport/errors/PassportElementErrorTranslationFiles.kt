package org.bezsahara.kittybot.telegram.classes.passport.errors


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an issue with the translated version of a document. The error is considered resolved when a file with the document translation change.
 * 
 * *[link](https://core.telegram.org/bots/api#passportelementerrortranslationfiles)*: https://core.telegram.org/bots/api#passportelementerrortranslationfiles
 * 
 * @param source Error source, must be translation_files
 * @param type Type of element of the user's Telegram Passport which has the issue, one of "passport", "driver_license", "identity_card", "internal_passport", "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration"
 * @param fileHashes List of base64-encoded file hashes
 * @param message Error message
 */
@Serializable
data class PassportElementErrorTranslationFiles(
    val source: String,
    val type: String,
    @SerialName("file_hashes") val fileHashes: List<String>,
    val message: String
) : PassportElementError

