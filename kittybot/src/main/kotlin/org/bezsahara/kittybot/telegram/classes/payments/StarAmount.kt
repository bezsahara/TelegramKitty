package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes an amount of Telegram Stars.
 * 
 * [link](https://core.telegram.org/bots/api#staramount): https://core.telegram.org/bots/api#staramount
 * 
 * @param amount Integer amount of Telegram Stars, rounded to 0; can be negative
 * @param nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars; from -999999999 to 999999999; can be negative if and only if amount is non-positive
 */
@Serializable
data class StarAmount(
    val amount: Long,
    @SerialName("nanostar_amount") val nanostarAmount: Long? = null
)

