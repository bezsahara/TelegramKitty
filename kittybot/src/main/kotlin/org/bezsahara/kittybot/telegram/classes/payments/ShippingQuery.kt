package org.bezsahara.kittybot.telegram.classes.payments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object contains information about an incoming shipping query.
 * 
 * *[link](https://core.telegram.org/bots/api#shippingquery)*: https://core.telegram.org/bots/api#shippingquery
 * 
 * @param id Unique query identifier
 * @param from User who sent the query
 * @param invoicePayload Bot specified invoice payload
 * @param shippingAddress User specified shipping address
 */
@Serializable
data class ShippingQuery(
    val id: String,
    val from: User,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("shipping_address") val shippingAddress: ShippingAddress
)

