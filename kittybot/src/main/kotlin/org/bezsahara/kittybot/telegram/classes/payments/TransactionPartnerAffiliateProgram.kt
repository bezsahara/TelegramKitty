package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.user.User


/**
 * Describes the affiliate program that issued the affiliate commission received via this transaction.
 * 
 * [link](https://core.telegram.org/bots/api#transactionpartneraffiliateprogram): https://core.telegram.org/bots/api#transactionpartneraffiliateprogram
 * 
 * @param type Type of the transaction partner, always "affiliate_program"
 * @param sponsorUser Optional. Information about the bot that sponsored the affiliate program
 * @param commissionPerMille The number of Telegram Stars received by the bot for each 1000 Telegram Stars received by the affiliate program sponsor from referred users
 */
@Serializable
data class TransactionPartnerAffiliateProgram(
    @SerialName("commission_per_mille") val commissionPerMille: Long,
    @SerialName("sponsor_user") val sponsorUser: User? = null
) : TransactionPartner {
    override val type: String = "affiliate_program"
}

