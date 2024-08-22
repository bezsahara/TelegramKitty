package org.bezsahara.kittybot.telegram.classes.passport.errors


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an issue with the front side of a document. The error is considered resolved when the file with the front side of the document changes.
 * 
 * *[link](https://core.telegram.org/bots/api#passportelementerrorfrontside)*: https://core.telegram.org/bots/api#passportelementerrorfrontside
 * 
 * @param source Error source, must be front_side
 * @param type The section of the user's Telegram Passport which has the issue, one of "passport", "driver_license", "identity_card", "internal_passport"
 * @param fileHash Base64-encoded hash of the file with the front side of the document
 * @param message Error message
 */
@Serializable
data class PassportElementErrorFrontSide(
    val source: String,
    val type: String,
    @SerialName("file_hash") val fileHash: String,
    val message: String
) : PassportElementError

