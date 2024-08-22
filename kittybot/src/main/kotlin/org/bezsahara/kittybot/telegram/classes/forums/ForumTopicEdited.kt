package org.bezsahara.kittybot.telegram.classes.forums


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about an edited forum topic.
 * 
 * *[link](https://core.telegram.org/bots/api#forumtopicedited)*: https://core.telegram.org/bots/api#forumtopicedited
 * 
 * @param name Optional. New name of the topic, if it was edited
 * @param iconCustomEmojiId Optional. New identifier of the custom emoji shown as the topic icon, if it was edited; an empty string if the icon was removed
 */
@Serializable
data class ForumTopicEdited(
    val name: String? = null,
    @SerialName("icon_custom_emoji_id") val iconCustomEmojiId: String? = null
)

