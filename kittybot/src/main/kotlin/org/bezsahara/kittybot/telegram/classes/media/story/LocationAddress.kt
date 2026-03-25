package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes the physical address of a location.
 * 
 * [link](https://core.telegram.org/bots/api#locationaddress): https://core.telegram.org/bots/api#locationaddress
 * 
 * @param countryCode The two-letter ISO 3166-1 alpha-2 country code of the country where the location is located
 * @param state Optional. State of the location
 * @param city Optional. City of the location
 * @param street Optional. Street address of the location
 */
@Serializable
data class LocationAddress(
    @SerialName("country_code") val countryCode: String,
    val state: String? = null,
    val city: String? = null,
    val street: String? = null
)

