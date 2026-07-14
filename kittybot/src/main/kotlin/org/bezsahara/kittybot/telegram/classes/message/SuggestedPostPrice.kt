package org.bezsahara.kittybot.telegram.classes.message

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind


/**
 * Describes the price of a suggested post.
 * 
 * [link](https://core.telegram.org/bots/api#suggestedpostprice): https://core.telegram.org/bots/api#suggestedpostprice
 * 
 * @param currency Currency in which the post will be paid. Currently, must be one of "XTR" for Telegram Stars or "TON" for TON grams.
 * @param amount The amount of the currency that will be paid for the post in the smallest units of the currency, i.e. Telegram Stars or nanograms. Currently, price in Telegram Stars must be between 5 and 100000, and price in nanograms must be between 10000000 and 10000000000000.
 */
@Serializable
data class SuggestedPostPrice(
    val currency: CurrencyKind,
    val amount: Long
)

