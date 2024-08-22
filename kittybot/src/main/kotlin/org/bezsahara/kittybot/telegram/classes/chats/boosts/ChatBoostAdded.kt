package org.bezsahara.kittybot.telegram.classes.chats.boosts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about a user boosting a chat.
 * 
 * *[link](https://core.telegram.org/bots/api#chatboostadded)*: https://core.telegram.org/bots/api#chatboostadded
 * 
 * @param boostCount Number of boosts added by the user
 */
@Serializable
data class ChatBoostAdded(
    @SerialName("boost_count") val boostCount: Long
)

