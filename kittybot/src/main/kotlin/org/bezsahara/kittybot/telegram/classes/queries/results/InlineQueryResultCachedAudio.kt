package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a link to an MP3 audio file stored on the Telegram servers. By default, this audio file will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the audio.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultcachedaudio)*: https://core.telegram.org/bots/api#inlinequeryresultcachedaudio
 * 
 * @param type Type of the result, must be audio
 * @param id Unique identifier for this result, 1-64 bytes
 * @param audioFileId A valid file identifier for the audio file
 * @param caption Optional. Caption, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the audio caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the audio
 */
@Serializable
data class InlineQueryResultCachedAudio(
    val id: String,
    @SerialName("audio_file_id") val audioFileId: String,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: String? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override val type: String = "audio"
}

