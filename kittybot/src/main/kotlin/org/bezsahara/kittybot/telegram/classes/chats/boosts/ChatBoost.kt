package org.bezsahara.kittybot.telegram.classes.chats.boosts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.boosts.sources.ChatBoostSource


/**
 * This object contains information about a chat boost.
 * 
 * *[link](https://core.telegram.org/bots/api#chatboost)*: https://core.telegram.org/bots/api#chatboost
 * 
 * @param boostId Unique identifier of the boost
 * @param addDate Point in time (Unix timestamp) when the chat was boosted
 * @param expirationDate Point in time (Unix timestamp) when the boost will automatically expire, unless the booster's Telegram Premium subscription is prolonged
 * @param source Source of the added boost
 */
@Serializable
data class ChatBoost(
    @SerialName("boost_id") val boostId: String,
    @SerialName("add_date") val addDate: Long,
    @SerialName("expiration_date") val expirationDate: Long,
    val source: ChatBoostSource
)

