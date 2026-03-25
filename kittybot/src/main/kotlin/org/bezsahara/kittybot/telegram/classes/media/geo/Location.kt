package org.bezsahara.kittybot.telegram.classes.media.geo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a point on the map.
 * 
 * [link](https://core.telegram.org/bots/api#location): https://core.telegram.org/bots/api#location
 * 
 * @param latitude Latitude as defined by the sender
 * @param longitude Longitude as defined by the sender
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 * @param livePeriod Optional. Time relative to the message sending date, during which the location can be updated; in seconds. For active live locations only.
 * @param heading Optional. The direction in which user is moving, in degrees; 1-360. For active live locations only.
 * @param proximityAlertRadius Optional. The maximum distance for proximity alerts about approaching another chat member, in meters. For sent live locations only.
 */
@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Double? = null,
    @SerialName("live_period") val livePeriod: Long? = null,
    val heading: Long? = null,
    @SerialName("proximity_alert_radius") val proximityAlertRadius: Long? = null
)

