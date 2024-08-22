package org.bezsahara.kittybot.telegram.classes.payments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents information about an order.
 * 
 * *[link](https://core.telegram.org/bots/api#orderinfo)*: https://core.telegram.org/bots/api#orderinfo
 * 
 * @param name Optional. User name
 * @param phoneNumber Optional. User's phone number
 * @param email Optional. User email
 * @param shippingAddress Optional. User shipping address
 */
@Serializable
data class OrderInfo(
    val name: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    val email: String? = null,
    @SerialName("shipping_address") val shippingAddress: ShippingAddress? = null
)

