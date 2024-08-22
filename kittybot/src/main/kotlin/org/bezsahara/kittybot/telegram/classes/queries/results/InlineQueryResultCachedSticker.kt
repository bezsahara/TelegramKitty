package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a link to a sticker stored on the Telegram servers. By default, this sticker will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the sticker.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultcachedsticker)*: https://core.telegram.org/bots/api#inlinequeryresultcachedsticker
 * 
 * @param type Type of the result, must be sticker
 * @param id Unique identifier for this result, 1-64 bytes
 * @param stickerFileId A valid file identifier of the sticker
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the sticker
 */
@Serializable
data class InlineQueryResultCachedSticker(
    val id: String,
    @SerialName("sticker_file_id") val stickerFileId: String,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
) : InlineQueryResult {
    override val type: String = "sticker"
}

