package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat


/**
 * This object represents a story.
 * 
 * [link](https://core.telegram.org/bots/api#story): https://core.telegram.org/bots/api#story
 * 
 * @param chat Chat that posted the story
 * @param id Unique identifier for the story in the chat
 */
@Serializable
data class Story(
    val chat: Chat,
    val id: Long
)

