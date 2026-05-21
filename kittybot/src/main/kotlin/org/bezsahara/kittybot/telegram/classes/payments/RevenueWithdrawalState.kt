package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.Serializable


@Serializable(with = RevenueWithdrawalStateSerializer::class)
sealed interface RevenueWithdrawalState {
    val type: String
}

