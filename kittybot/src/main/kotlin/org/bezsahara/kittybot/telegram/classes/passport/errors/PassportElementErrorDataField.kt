package org.bezsahara.kittybot.telegram.classes.passport.errors


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents an issue in one of the data fields that was provided by the user. The error is considered resolved when the field's value changes.
 * 
 * *[link](https://core.telegram.org/bots/api#passportelementerrordatafield)*: https://core.telegram.org/bots/api#passportelementerrordatafield
 * 
 * @param source Error source, must be data
 * @param type The section of the user's Telegram Passport which has the error, one of "personal_details", "passport", "driver_license", "identity_card", "internal_passport", "address"
 * @param fieldName Name of the data field which has the error
 * @param dataHash Base64-encoded data hash
 * @param message Error message
 */
@Serializable
data class PassportElementErrorDataField(
    val source: String,
    val type: String,
    @SerialName("field_name") val fieldName: String,
    @SerialName("data_hash") val dataHash: String,
    val message: String
) : PassportElementError

