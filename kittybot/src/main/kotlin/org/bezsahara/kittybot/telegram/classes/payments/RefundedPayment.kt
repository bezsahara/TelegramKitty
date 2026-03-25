package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind
import kotlinx.serialization.Serializable


/**
 * This object contains basic information about a refunded payment.
 * 
 * [link](https://core.telegram.org/bots/api#refundedpayment): https://core.telegram.org/bots/api#refundedpayment
 * 
 * @param currency Three-letter ISO 4217 currency code, or "XTR" for payments in Telegram Stars. Currently, always "XTR"
 * @param totalAmount Total refunded price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45, total_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot-specified invoice payload
 * @param telegramPaymentChargeId Telegram payment identifier
 * @param providerPaymentChargeId Optional. Provider payment identifier
 */
@Serializable
data class RefundedPayment(
    val currency: CurrencyKind,
    @SerialName("total_amount") val totalAmount: Long,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String,
    @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String? = null
)

