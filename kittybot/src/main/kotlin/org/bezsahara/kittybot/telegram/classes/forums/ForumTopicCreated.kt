package org.bezsahara.kittybot.telegram.classes.forums


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about a new forum topic created in the chat.
 * 
 * *[link](https://core.telegram.org/bots/api#forumtopiccreated)*: https://core.telegram.org/bots/api#forumtopiccreated
 * 
 * @param name Name of the topic
 * @param iconColor Color of the topic icon in RGB format
 * @param iconCustomEmojiId Optional. Unique identifier of the custom emoji shown as the topic icon
 */
@Serializable
data class ForumTopicCreated(
    val name: String,
    @SerialName("icon_color") val iconColor: Long,
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null
)

