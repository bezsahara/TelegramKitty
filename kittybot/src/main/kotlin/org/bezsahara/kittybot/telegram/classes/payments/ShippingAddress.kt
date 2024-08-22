package org.bezsahara.kittybot.telegram.classes.payments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a shipping address.
 * 
 * *[link](https://core.telegram.org/bots/api#shippingaddress)*: https://core.telegram.org/bots/api#shippingaddress
 * 
 * @param countryCode Two-letter ISO 3166-1 alpha-2 country code
 * @param state State, if applicable
 * @param city City
 * @param streetLine1 First line for the address
 * @param streetLine2 Second line for the address
 * @param postCode Address post code
 */
@Serializable
data class ShippingAddress(
    @SerialName("country_code") val countryCode: String,
    val state: String,
    val city: String,
    @SerialName("street_line1") val streetLine1: String,
    @SerialName("street_line2") val streetLine2: String,
    @SerialName("post_code") val postCode: String
)

