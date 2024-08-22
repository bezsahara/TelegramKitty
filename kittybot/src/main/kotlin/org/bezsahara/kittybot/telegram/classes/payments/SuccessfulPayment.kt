package org.bezsahara.kittybot.telegram.classes.payments


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object contains basic information about a successful payment.
 * 
 * *[link](https://core.telegram.org/bots/api#successfulpayment)*: https://core.telegram.org/bots/api#successfulpayment
 * 
 * @param currency Three-letter ISO 4217 currency code
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot specified invoice payload
 * @param shippingOptionId Optional. Identifier of the shipping option chosen by the user
 * @param orderInfo Optional. Order information provided by the user
 * @param telegramPaymentChargeId Telegram payment identifier
 * @param providerPaymentChargeId Provider payment identifier
 */
@Serializable
data class SuccessfulPayment(
    val currency: String,
    @SerialName("total_amount") val totalAmount: Long,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("shipping_option_id") val shippingOptionId: String? = null,
    @SerialName("order_info") val orderInfo: OrderInfo? = null,
    @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String,
    @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String
)

