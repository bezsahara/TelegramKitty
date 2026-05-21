package org.bezsahara.kittybot.telegram.classes.passport

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.values.PassportElementType


/**
 * Represents an issue with the reverse side of a document. The error is considered resolved when the file with reverse side of the document changes.
 * 
 * [link](https://core.telegram.org/bots/api#passportelementerrorreverseside): https://core.telegram.org/bots/api#passportelementerrorreverseside
 * 
 * @param source Error source, must be reverse_side
 * @param type The section of the user's Telegram Passport which has the issue, one of "driver_license", "identity_card"
 * @param fileHash Base64-encoded hash of the file with the reverse side of the document
 * @param message Error message
 */
@Serializable
data class PassportElementErrorReverseSide(
    val type: PassportElementType,
    @SerialName("file_hash") val fileHash: String,
    val message: String
) : PassportElementError {
    override val source: String = "reverse_side"
}

