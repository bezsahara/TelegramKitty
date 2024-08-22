package org.bezsahara.kittybot.telegram.classes.locations


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * This object represents a venue.
 * 
 * *[link](https://core.telegram.org/bots/api#venue)*: https://core.telegram.org/bots/api#venue
 * 
 * @param location Venue location. Can't be a live location
 * @param title Name of the venue
 * @param address Address of the venue
 * @param foursquareId Optional. Foursquare identifier of the venue
 * @param foursquareType Optional. Foursquare type of the venue. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
 * @param googlePlaceId Optional. Google Places identifier of the venue
 * @param googlePlaceType Optional. Google Places type of the venue. (See supported types.)
 */
@Serializable
data class Venue(
    val location: Location,
    val title: String,
    val address: String,
    @SerialName("foursquare_id") val foursquareId: String? = null,
    @SerialName("foursquare_type") val foursquareType: String? = null,
    @SerialName("google_place_id") val googlePlaceId: String? = null,
    @SerialName("google_place_type") val googlePlaceType: String? = null
)

