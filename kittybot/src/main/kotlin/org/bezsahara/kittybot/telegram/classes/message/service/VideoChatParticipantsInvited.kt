package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about new members invited to a video chat.
 * 
 * [link](https://core.telegram.org/bots/api#videochatparticipantsinvited): https://core.telegram.org/bots/api#videochatparticipantsinvited
 * 
 * @param users New members that were invited to the video chat
 */
@Serializable
data class VideoChatParticipantsInvited(
    val users: List<User>
)

