package org.bezsahara.kittybot.telegram.classes.passport


import kotlinx.serialization.Serializable


/**
 * Describes Telegram Passport data shared with the bot by the user.
 * 
 * *[link](https://core.telegram.org/bots/api#passportdata)*: https://core.telegram.org/bots/api#passportdata
 * 
 * @param data Array with information about documents and other Telegram Passport elements that was shared with the bot
 * @param credentials Encrypted credentials required to decrypt the data
 */
@Serializable
data class PassportData(
    val data: List<EncryptedPassportElement>,
    val credentials: EncryptedCredentials
)

