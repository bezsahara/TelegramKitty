package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a link to a voice recording in an .OGG container encoded with OPUS. By default, this voice recording will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the the voice message.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultvoice)*: https://core.telegram.org/bots/api#inlinequeryresultvoice
 * 
 * @param type Type of the result, must be voice
 * @param id Unique identifier for this result, 1-64 bytes
 * @param voiceUrl A valid URL for the voice recording
 * @param title Recording title
 * @param caption Optional. Caption, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the voice message caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param voiceDuration Optional. Recording duration in seconds
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the voice recording
 */
@Serializable
data class InlineQueryResultVoice(
    val id: String,
    @SerialName("voice_url") val voiceUrl: String,
    val title: String,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("voice_duration") val voiceDuration: Long? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override val type: String = "voice"
}

