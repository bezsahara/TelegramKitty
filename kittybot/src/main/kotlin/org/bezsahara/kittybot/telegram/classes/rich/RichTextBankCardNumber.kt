package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * A text with a bank card number.
 *
 * [link](https://core.telegram.org/bots/api#richtextbankcardnumber): https://core.telegram.org/bots/api#richtextbankcardnumber
 *
 * @param type Type of the rich text, always "bank_card_number"
 * @param text The text
 * @param bankCardNumber The bank card number
 */
@Serializable
data class RichTextBankCardNumber(
    val text: RichText,
    @SerialName("bank_card_number") val bankCardNumber: String
) : RichText {
    override val type: String = "bank_card_number"
}
