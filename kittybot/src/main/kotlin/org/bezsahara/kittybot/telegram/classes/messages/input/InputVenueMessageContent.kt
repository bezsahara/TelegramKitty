package org.bezsahara.kittybot.telegram.classes.messages.input


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents the content of a venue message to be sent as the result of an inline query.
 * 
 * *[link](https://core.telegram.org/bots/api#inputvenuemessagecontent)*: https://core.telegram.org/bots/api#inputvenuemessagecontent
 * 
 * @param latitude Latitude of the venue in degrees
 * @param longitude Longitude of the venue in degrees
 * @param title Name of the venue
 * @param address Address of the venue
 * @param foursquareId Optional. Foursquare identifier of the venue, if known
 * @param foursquareType Optional. Foursquare type of the venue, if known. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
 * @param googlePlaceId Optional. Google Places identifier of the venue
 * @param googlePlaceType Optional. Google Places type of the venue. (See supported types.)
 */
@Serializable
data class InputVenueMessageContent(
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val address: String,
    @SerialName("foursquare_id") val foursquareId: String? = null,
    @SerialName("foursquare_type") val foursquareType: String? = null,
    @SerialName("google_place_id") val googlePlaceId: String? = null,
    @SerialName("google_place_type") val googlePlaceType: String? = null
) : InputMessageContent

