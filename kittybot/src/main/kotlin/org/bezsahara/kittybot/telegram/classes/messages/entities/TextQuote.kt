package org.bezsahara.kittybot.telegram.classes.messages.entities


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object contains information about the quoted part of a message that is replied to by the given message.
 * 
 * *[link](https://core.telegram.org/bots/api#textquote)*: https://core.telegram.org/bots/api#textquote
 * 
 * @param text Text of the quoted part of a message that is replied to by the given message
 * @param entities Optional. Special entities that appear in the quote. Currently, only bold, italic, underline, strikethrough, spoiler, and custom_emoji entities are kept in quotes.
 * @param position Approximate quote position in the original message in UTF-16 code units as specified by the sender
 * @param isManual Optional. True, if the quote was chosen manually by the message sender. Otherwise, the quote was added automatically by the server.
 */
@Serializable
data class TextQuote(
    val text: String,
    val entities: List<MessageEntity>? = null,
    val position: Long,
    @SerialName("is_manual") val isManual: Boolean? = null
)

