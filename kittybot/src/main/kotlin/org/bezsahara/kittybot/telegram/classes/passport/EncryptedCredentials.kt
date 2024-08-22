package org.bezsahara.kittybot.telegram.classes.passport


import kotlinx.serialization.Serializable


/**
 * Describes data required for decrypting and authenticating EncryptedPassportElement. See the Telegram Passport Documentation for a complete description of the data decryption and authentication processes.
 * 
 * *[link](https://core.telegram.org/bots/api#encryptedcredentials)*: https://core.telegram.org/bots/api#encryptedcredentials
 * 
 * @param data Base64-encoded encrypted JSON-serialized data with unique user's payload, data hashes and secrets required for EncryptedPassportElement decryption and authentication
 * @param hash Base64-encoded data hash for data authentication
 * @param secret Base64-encoded secret, encrypted with the bot's public RSA key, required for data decryption
 */
@Serializable
data class EncryptedCredentials(
    val data: String,
    val hash: String,
    val secret: String
)

