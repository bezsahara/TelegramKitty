package org.bezsahara.kittybot.telegram.classes.chats.video


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about a video chat scheduled in the chat.
 * 
 * *[link](https://core.telegram.org/bots/api#videochatscheduled)*: https://core.telegram.org/bots/api#videochatscheduled
 * 
 * @param startDate Point in time (Unix timestamp) when the video chat is supposed to be started by a chat administrator
 */
@Serializable
data class VideoChatScheduled(
    @SerialName("start_date") val startDate: Long
)

