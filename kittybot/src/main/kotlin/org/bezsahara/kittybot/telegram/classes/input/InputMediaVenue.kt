package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder


/**
 * Represents a venue to be sent.
 * 
 * [link](https://core.telegram.org/bots/api#inputmediavenue): https://core.telegram.org/bots/api#inputmediavenue
 * 
 * @param type Type of the result, must be venue
 * @param latitude Latitude of the location
 * @param longitude Longitude of the location
 * @param title Name of the venue
 * @param address Address of the venue
 * @param foursquareId Optional. Foursquare identifier of the venue
 * @param foursquareType Optional. Foursquare type of the venue, if known. (For example, "arts_entertainment/default", "arts_entertainment/aquarium" or "food/icecream".)
 * @param googlePlaceId Optional. Google Places identifier of the venue
 * @param googlePlaceType Optional. Google Places type of the venue. (See supported types.)
 */
@Serializable
data class InputMediaVenue(
    val latitude: Double,
    val longitude: Double,
    val title: String,
    val address: String,
    @SerialName("foursquare_id") val foursquareId: String? = null,
    @SerialName("foursquare_type") val foursquareType: String? = null,
    @SerialName("google_place_id") val googlePlaceId: String? = null,
    @SerialName("google_place_type") val googlePlaceType: String? = null
) : InputPollMedia, InputPollOptionMedia {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
    }
    override val type: String = "venue"
}

