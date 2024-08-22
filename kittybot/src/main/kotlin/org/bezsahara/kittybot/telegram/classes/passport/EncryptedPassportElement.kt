package org.bezsahara.kittybot.telegram.classes.passport


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes documents or other Telegram Passport elements shared with the bot by the user.
 * 
 * *[link](https://core.telegram.org/bots/api#encryptedpassportelement)*: https://core.telegram.org/bots/api#encryptedpassportelement
 * 
 * @param type Element type. One of "personal_details", "passport", "driver_license", "identity_card", "internal_passport", "address", "utility_bill", "bank_statement", "rental_agreement", "passport_registration", "temporary_registration", "phone_number", "email".
 * @param data Optional. Base64-encoded encrypted Telegram Passport element data provided by the user; available only for "personal_details", "passport", "driver_license", "identity_card", "internal_passport" and "address" types. Can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param phoneNumber Optional. User's verified phone number; available only for "phone_number" type
 * @param email Optional. User's verified email address; available only for "email" type
 * @param files Optional. Array of encrypted files with documents provided by the user; available only for "utility_bill", "bank_statement", "rental_agreement", "passport_registration" and "temporary_registration" types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param frontSide Optional. Encrypted file with the front side of the document, provided by the user; available only for "passport", "driver_license", "identity_card" and "internal_passport". The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param reverseSide Optional. Encrypted file with the reverse side of the document, provided by the user; available only for "driver_license" and "identity_card". The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param selfie Optional. Encrypted file with the selfie of the user holding a document, provided by the user; available if requested for "passport", "driver_license", "identity_card" and "internal_passport". The file can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param translation Optional. Array of encrypted files with translated versions of documents provided by the user; available if requested for "passport", "driver_license", "identity_card", "internal_passport", "utility_bill", "bank_statement", "rental_agreement", "passport_registration" and "temporary_registration" types. Files can be decrypted and verified using the accompanying EncryptedCredentials.
 * @param hash Base64-encoded element hash for using in PassportElementErrorUnspecified
 */
@Serializable
data class EncryptedPassportElement(
    val type: String,
    val data: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    val email: String? = null,
    val files: List<PassportFile>? = null,
    @SerialName("front_side") val frontSide: PassportFile? = null,
    @SerialName("reverse_side") val reverseSide: PassportFile? = null,
    val selfie: PassportFile? = null,
    val translation: List<PassportFile>? = null,
    val hash: String
)

