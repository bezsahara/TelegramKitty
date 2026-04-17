package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.SerialName
import kotlin.collections.List
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity


/**
 * This object contains information about the quoted part of a message that is replied to by the given message.
 * 
 * [link](https://core.telegram.org/bots/api#textquote): https://core.telegram.org/bots/api#textquote
 * 
 * @param text Text of the quoted part of a message that is replied to by the given message
 * @param entities Optional. Special entities that appear in the quote. Currently, only bold, italic, underline, strikethrough, spoiler, custom_emoji, and date_time entities are kept in quotes.
 * @param position Approximate quote position in the original message in UTF-16 code units as specified by the sender
 * @param isManual Optional. True, if the quote was chosen manually by the message sender. Otherwise, the quote was added automatically by the server.
 */
@Serializable
data class TextQuote(
    val text: String,
    val position: Long,
    val entities: List<MessageEntity>? = null,
    @SerialName("is_manual") val isManual: Boolean? = null
)

