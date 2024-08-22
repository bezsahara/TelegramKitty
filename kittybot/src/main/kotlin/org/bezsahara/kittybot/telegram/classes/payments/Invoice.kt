package org.bezsahara.kittybot.telegram.classes.payments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object contains basic information about an invoice.
 * 
 * *[link](https://core.telegram.org/bots/api#invoice)*: https://core.telegram.org/bots/api#invoice
 * 
 * @param title Product name
 * @param description Product description
 * @param startParameter Unique bot deep-linking parameter that can be used to generate this invoice
 * @param currency Three-letter ISO 4217 currency code
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 */
@Serializable
data class Invoice(
    val title: String,
    val description: String,
    @SerialName("start_parameter") val startParameter: String,
    val currency: String,
    @SerialName("total_amount") val totalAmount: Long
)

