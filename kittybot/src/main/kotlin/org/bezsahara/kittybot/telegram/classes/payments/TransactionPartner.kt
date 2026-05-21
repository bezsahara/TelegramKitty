package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.Serializable


@Serializable(with = TransactionPartnerSerializer::class)
sealed interface TransactionPartner {
    val type: String
}

