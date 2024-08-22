package org.bezsahara.kittybot.telegram.classes.polls


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chats.Chat
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents an answer of a user in a non-anonymous poll.
 * 
 * *[link](https://core.telegram.org/bots/api#pollanswer)*: https://core.telegram.org/bots/api#pollanswer
 * 
 * @param pollId Unique poll identifier
 * @param voterChat Optional. The chat that changed the answer to the poll, if the voter is anonymous
 * @param user Optional. The user that changed the answer to the poll, if the voter isn't anonymous
 * @param optionIds 0-based identifiers of chosen answer options. May be empty if the vote was retracted.
 */
@Serializable
data class PollAnswer(
    @SerialName("poll_id") val pollId: String,
    @SerialName("voter_chat") val voterChat: Chat? = null,
    val user: User? = null,
    @SerialName("option_ids") val optionIds: List<Long>
)

