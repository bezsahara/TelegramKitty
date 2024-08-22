package org.bezsahara.kittybot.telegram.classes.forums


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a forum topic.
 * 
 * *[link](https://core.telegram.org/bots/api#forumtopic)*: https://core.telegram.org/bots/api#forumtopic
 * 
 * @param messageThreadId Unique identifier of the forum topic
 * @param name Name of the topic
 * @param iconColor Color of the topic icon in RGB format
 * @param iconCustomEmojiId Optional. Unique identifier of the custom emoji shown as the topic icon
 */
@Serializable
data class ForumTopic(
    @SerialName("message_thread_id") val messageThreadId: Long,
    val name: String,
    @SerialName("icon_color") val iconColor: Long,
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null
)

