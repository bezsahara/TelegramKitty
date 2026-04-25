package org.bezsahara.kittybot.telegram.classes.message.polls

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.values.ParseMode
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * This object contains information about one answer option in a poll to be sent.
 * 
 * [link](https://core.telegram.org/bots/api#inputpolloption): https://core.telegram.org/bots/api#inputpolloption
 * 
 * @param text Option text, 1-100 characters
 * @param textParseMode Optional. Mode for parsing entities in the text. See formatting options for more details. Currently, only custom emoji entities are allowed
 * @param textEntities Optional. A JSON-serialized list of special entities that appear in the poll option text. It can be specified instead of text_parse_mode
 */
@Serializable
data class InputPollOption(
    val text: String,
    @SerialName("text_parse_mode") val textParseMode: ParseMode? = null,
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null
)

