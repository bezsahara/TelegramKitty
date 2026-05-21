package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboard.InlineKeyboardMarkup


/**
 * Represents a link to an article or web page.
 * 
 * [link](https://core.telegram.org/bots/api#inlinequeryresultarticle): https://core.telegram.org/bots/api#inlinequeryresultarticle
 * 
 * @param type Type of the result, must be article
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param title Title of the result
 * @param inputMessageContent Content of the message to be sent
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param url Optional. URL of the result
 * @param description Optional. Short description of the result
 * @param thumbnailUrl Optional. Url of the thumbnail for the result
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
data class InlineQueryResultArticle(
    val id: String,
    val title: String,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    val url: String? = null,
    val description: String? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("thumbnail_width") val thumbnailWidth: Long? = null,
    @SerialName("thumbnail_height") val thumbnailHeight: Long? = null
) : InlineQueryResult {
    override val type: String = "article"
}

