package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * Describes a topic of a direct messages chat.
 * 
 * [link](https://core.telegram.org/bots/api#directmessagestopic): https://core.telegram.org/bots/api#directmessagestopic
 * 
 * @param topicId Unique identifier of the topic. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param user Optional. Information about the user that created the topic. Currently, it is always present.
 */
@Serializable
data class DirectMessagesTopic(
    @SerialName("topic_id") val topicId: Long,
    val user: User? = null
)

