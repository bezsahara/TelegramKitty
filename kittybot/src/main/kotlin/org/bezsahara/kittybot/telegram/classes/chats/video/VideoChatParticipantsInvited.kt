package org.bezsahara.kittybot.telegram.classes.chats.video


import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents a service message about new members invited to a video chat.
 * 
 * *[link](https://core.telegram.org/bots/api#videochatparticipantsinvited)*: https://core.telegram.org/bots/api#videochatparticipantsinvited
 * 
 * @param users New members that were invited to the video chat
 */
@Serializable
data class VideoChatParticipantsInvited(
    val users: List<User>
)

