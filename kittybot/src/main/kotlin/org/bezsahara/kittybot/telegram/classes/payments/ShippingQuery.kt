package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.ShippingAddress
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * This object contains information about an incoming shipping query.
 * 
 * [link](https://core.telegram.org/bots/api#shippingquery): https://core.telegram.org/bots/api#shippingquery
 * 
 * @param id Unique query identifier
 * @param from User who sent the query
 * @param invoicePayload Bot-specified invoice payload
 * @param shippingAddress User specified shipping address
 */
@Serializable
data class ShippingQuery(
    val id: String,
    val from: User,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("shipping_address") val shippingAddress: ShippingAddress
)

