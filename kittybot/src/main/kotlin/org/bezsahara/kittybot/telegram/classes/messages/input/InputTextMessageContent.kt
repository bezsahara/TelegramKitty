package org.bezsahara.kittybot.telegram.classes.messages.input


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.previews.LinkPreviewOptions


/**
 * Represents the content of a text message to be sent as the result of an inline query.
 * 
 * *[link](https://core.telegram.org/bots/api#inputtextmessagecontent)*: https://core.telegram.org/bots/api#inputtextmessagecontent
 * 
 * @param messageText Text of the message to be sent, 1-4096 characters
 * @param parseMode Optional. Mode for parsing entities in the message text. See formatting options for more details.
 * @param entities Optional. List of special entities that appear in message text, which can be specified instead of parse_mode
 * @param linkPreviewOptions Optional. Link preview generation options for the message
 */
@Serializable
data class InputTextMessageContent(
    @SerialName("message_text") val messageText: String,
    @SerialName("parse_mode") val parseMode: String? = null,
    val entities: List<MessageEntity>? = null,
    @SerialName("link_preview_options") val linkPreviewOptions: LinkPreviewOptions? = null
) : InputMessageContent

