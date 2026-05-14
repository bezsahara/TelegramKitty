package org.bezsahara.kittybot.telegram.classes.message.polls

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.message.polls.PollMedia
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * This object contains information about one answer option in a poll.
 * 
 * [link](https://core.telegram.org/bots/api#polloption): https://core.telegram.org/bots/api#polloption
 * 
 * @param persistentId Unique identifier of the option, persistent on option addition and deletion
 * @param text Option text, 1-100 characters
 * @param textEntities Optional. Special entities that appear in the option text. Currently, only custom emoji entities are allowed in poll option texts
 * @param media Optional. Media added to the poll option
 * @param voterCount Number of users who voted for this option; may be 0 if unknown
 * @param addedByUser Optional. User who added the option; omitted if the option wasn't added by a user after poll creation
 * @param addedByChat Optional. Chat that added the option; omitted if the option wasn't added by a chat after poll creation
 * @param additionDate Optional. Point in time (Unix timestamp) when the option was added; omitted if the option existed in the original poll
 */
@Serializable
data class PollOption(
    @SerialName("persistent_id") val persistentId: String,
    val text: String,
    @SerialName("voter_count") val voterCount: Long,
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
    val media: PollMedia? = null,
    @SerialName("added_by_user") val addedByUser: User? = null,
    @SerialName("added_by_chat") val addedByChat: Chat? = null,
    @SerialName("addition_date") val additionDate: Long? = null
)

