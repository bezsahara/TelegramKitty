package org.bezsahara.kittybot.telegram.classes.messages.input


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents the content of a location message to be sent as the result of an inline query.
 * 
 * *[link](https://core.telegram.org/bots/api#inputlocationmessagecontent)*: https://core.telegram.org/bots/api#inputlocationmessagecontent
 * 
 * @param latitude Latitude of the location in degrees
 * @param longitude Longitude of the location in degrees
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Period in seconds during which the location can be updated, should be between 60 and 86400, or 0x7FFFFFFF for live locations that can be edited indefinitely.
 * @param heading Optional. For live locations, a direction in which the user is moving, in degrees. Must be between 1 and 360 if specified.
 * @param proximityAlertRadius Optional. For live locations, a maximum distance for proximity alerts about approaching another chat member, in meters. Must be between 1 and 100000 if specified.
 */
@Serializable
data class InputLocationMessageContent(
    val latitude: Float,
    val longitude: Float,
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Float? = null,
    @SerialName("live_period") val livePeriod: Long? = null,
    val heading: Long? = null,
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Long? = null
) : InputMessageContent

