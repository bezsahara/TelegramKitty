package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about a video chat ended in the chat.
 * 
 * [link](https://core.telegram.org/bots/api#videochatended): https://core.telegram.org/bots/api#videochatended
 * 
 * @param duration Video chat duration in seconds
 */
@Serializable
data class VideoChatEnded(
    val duration: Long
)

