package org.bezsahara.kittybot.telegram.classes.payments

import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice
import kotlinx.serialization.SerialName
import kotlin.collections.List
import kotlinx.serialization.Serializable


/**
 * This object represents one shipping option.
 * 
 * [link](https://core.telegram.org/bots/api#shippingoption): https://core.telegram.org/bots/api#shippingoption
 * 
 * @param id Shipping option identifier
 * @param title Option title
 * @param prices List of price portions
 */
@Serializable
data class ShippingOption(
    val id: String,
    val title: String,
    val prices: List<LabeledPrice>
)

