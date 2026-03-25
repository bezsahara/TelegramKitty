package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat


/**
 * Contains information about the affiliate that received a commission via this transaction.
 * 
 * [link](https://core.telegram.org/bots/api#affiliateinfo): https://core.telegram.org/bots/api#affiliateinfo
 * 
 * @param affiliateUser Optional. The bot or the user that received an affiliate commission if it was received by a bot or a user
 * @param affiliateChat Optional. The chat that received an affiliate commission if it was received by a chat
 * @param commissionPerMille The number of Telegram Stars received by the affiliate for each 1000 Telegram Stars received by the bot from referred users
 * @param amount Integer amount of Telegram Stars received by the affiliate from the transaction, rounded to 0; can be negative for refunds
 * @param nanostarAmount Optional. The number of 1/1000000000 shares of Telegram Stars received by the affiliate; from -999999999 to 999999999; can be negative for refunds
 */
@Serializable
data class AffiliateInfo(
    @SerialName("commission_per_mille") val commissionPerMille: Long,
    val amount: Long,
    @SerialName("affiliate_user") val affiliateUser: User? = null,
    @SerialName("affiliate_chat") val affiliateChat: Chat? = null,
    @SerialName("nanostar_amount") val nanostarAmount: Long? = null
)

