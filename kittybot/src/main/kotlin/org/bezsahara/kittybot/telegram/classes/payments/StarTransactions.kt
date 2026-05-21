package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.Serializable


/**
 * Contains a list of Telegram Star transactions.
 * 
 * [link](https://core.telegram.org/bots/api#startransactions): https://core.telegram.org/bots/api#startransactions
 * 
 * @param transactions The list of transactions
 */
@Serializable
data class StarTransactions(
    val transactions: List<StarTransaction>
)

