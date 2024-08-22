package org.bezsahara.kittybot.telegram.classes.payments


import kotlinx.serialization.Serializable


/**
 * This object represents a portion of the price for goods or services.
 * 
 * *[link](https://core.telegram.org/bots/api#labeledprice)*: https://core.telegram.org/bots/api#labeledprice
 * 
 * @param label Portion label
 * @param amount Price of the product in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 */
@Serializable
data class LabeledPrice(
    val label: String,
    val amount: Long
)

