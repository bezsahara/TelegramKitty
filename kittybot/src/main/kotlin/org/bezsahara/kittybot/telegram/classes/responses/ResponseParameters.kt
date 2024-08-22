package org.bezsahara.kittybot.telegram.classes.responses


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * Describes why a request was unsuccessful.
 * 
 * *[link](https://core.telegram.org/bots/api#responseparameters)*: https://core.telegram.org/bots/api#responseparameters
 * 
 * @param migrateToChatId Optional. The group has been migrated to a supergroup with the specified identifier. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a signed 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param retryAfter Optional. In case of exceeding flood control, the number of seconds left to wait before the request can be repeated
 */
@Serializable
data class ResponseParameters(
    @SerialName("migrate_to_chat_id") val migrateToChatId: Long? = null,
    @SerialName("retry_after") val retryAfter: Long? = null
)

