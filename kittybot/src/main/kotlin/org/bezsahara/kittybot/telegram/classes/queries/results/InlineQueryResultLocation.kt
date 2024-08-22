package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup
import org.bezsahara.kittybot.telegram.classes.messages.input.InputMessageContent


/**
 * Represents a location on a map. By default, the location will be sent by the user. Alternatively, you can use input_message_content to send a message with the specified content instead of the location.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultlocation)*: https://core.telegram.org/bots/api#inlinequeryresultlocation
 * 
 * @param type Type of the result, must be location
 * @param id Unique identifier for this result, 1-64 Bytes
 * @param latitude Location latitude in degrees
 * @param longitude Location longitude in degrees
 * @param title Location title
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Period in seconds during which the location can be updated, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
 * @param heading Optional. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius Optional. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 * @param replyMarkup Optional. Inline keyboard attached to the message
 * @param inputMessageContent Optional. Content of the message to be sent instead of the location
 * @param thumbnailUrl Optional. Url of the thumbnail for the result
 * @param thumbnailWidth Optional. Thumbnail width
 * @param thumbnailHeight Optional. Thumbnail height
 */
@Serializable
data class InlineQueryResultLocation(
    val id: String,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,
    @SerialName("live_period") val livePeriod: Long? = null,
    val heading: Long? = null,
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Long? = null,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
    @SerialName("input_message_content") val inputMessageContent: InputMessageContent? = null,
    @SerialName("thumbnail_url") val thumbnailUrl: String? = null,
    @SerialName("thumbnail_width") val thumbnailWidth: Long? = null,
    @SerialName("thumbnail_height") val thumbnailHeight: Long? = null,
) : InlineQueryResult {
    override val type: String = "location"
}

