package org.bezsahara.kittybot.telegram.classes.chats.boosts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat
import org.bezsahara.kittybot.telegram.classes.chats.boosts.sources.ChatBoostSource


/**
 * This object represents a boost removed from a chat.
 * 
 * *[link](https://core.telegram.org/bots/api#chatboostremoved)*: https://core.telegram.org/bots/api#chatboostremoved
 * 
 * @param chat Chat which was boosted
 * @param boostId Unique identifier of the boost
 * @param removeDate Point in time (Unix timestamp) when the boost was removed
 * @param source Source of the removed boost
 */
@Serializable
data class ChatBoostRemoved(
    val chat: Chat,
    @SerialName("boost_id") val boostId: String,
    @SerialName("remove_date") val removeDate: Long,
    val source: ChatBoostSource
)

