package org.bezsahara.kittybot.telegram.classes.inline

import org.bezsahara.kittybot.telegram.classes.inline.InputMessageContent
import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.values.ParseMode
import org.bezsahara.kittybot.telegram.values.InlineQueryResultType
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.inline.InlineQueryResult


/**
 * Represents a link to an animated GIF file stored on the Telegram servers. By default, this animated GIF file will be sent by the user with an optional caption. Alternatively, you can use input_message_content to send a message with specified content instead of the animation.
 * 
 * [link](https://core.telegram.org/bots/api#inlinequeryresultcachedgif): https://core.telegram.org/bots/api#inlinequeryresultcachedgif
 * 
 * @param type Type of the result, must be gif
 * @param id Unique identifier for this result, 1-64 bytes
 * @param gifFileId A valid file identifier for the GIF file
 * @param title Optional. Title for the result
 * @param caption Optional. Caption of the GIF file to be sent, 0-1024 characters after entities parsing
 * @param parseMode Optional. Mode for parsing entities in the caption. See formatting options for more details.
 * @param captionEntities Optional. List of special entities that appear in the caption, which can be specified instead of parse_mode
 * @param showCaptionAboveMedia Optional. Pass True, if the caption must be shown above the message media
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the GIF animation
 */
@Serializable
data class InlineQueryResultCachedGif(
    val id: String,
    @SerialName("gif_file_id") val gifFileId: String,
    val title: String? = null,
    val caption: String? = null,
    @SerialName("parse_mode") val parseMode: ParseMode? = null,
    @SerialName("caption_entities") val captionEntities: List<MessageEntity>? = null,
    @SerialName("show_caption_above_media") val showCaptionAboveMedia: Boolean? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null
) : InlineQueryResult {
    override val type: InlineQueryResultType = InlineQueryResultType.GIF
}

