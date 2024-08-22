package org.bezsahara.kittybot.telegram.classes.passport.errors


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an issue in an unspecified place. The error is considered resolved when new data is added.
 * 
 * *[link](https://core.telegram.org/bots/api#passportelementerrorunspecified)*: https://core.telegram.org/bots/api#passportelementerrorunspecified
 * 
 * @param source Error source, must be unspecified
 * @param type Type of element of the user's Telegram Passport which has the issue
 * @param elementHash Base64-encoded element hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorUnspecified(
    val source: String,
    val type: String,
    @SerialName("element_hash") val elementHash: String,
    val message: String
) : PassportElementError

