package org.bezsahara.kittybot.telegram.classes.payments

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.business.CurrencyKind
import org.bezsahara.kittybot.telegram.classes.payments.OrderInfo
import kotlinx.serialization.Serializable


/**
 * This object contains basic information about a successful payment. Note that if the buyer initiates a chargeback with the relevant payment provider following this transaction, the funds may be debited from your balance. This is outside of Telegram's control.
 * 
 * [link](https://core.telegram.org/bots/api#successfulpayment): https://core.telegram.org/bots/api#successfulpayment
 * 
 * @param currency Three-letter ISO 4217 currency code, or "XTR" for payments in Telegram Stars
 * @param totalAmount Total price in the smallest units of the currency (integer, not float/double). For example, for a price of US$ 1.45 pass amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies).
 * @param invoicePayload Bot-specified invoice payload
 * @param subscriptionExpirationDate Optional. Expiration date of the subscription, in Unix time; for recurring payments only
 * @param isRecurring Optional. True, if the payment is a recurring payment for a subscription
 * @param isFirstRecurring Optional. True, if the payment is the first payment for a subscription
 * @param shippingOptionId Optional. Identifier of the shipping option chosen by the user
 * @param orderInfo Optional. Order information provided by the user
 * @param telegramPaymentChargeId Telegram payment identifier
 * @param providerPaymentChargeId Provider payment identifier
 */
@Serializable
data class SuccessfulPayment(
    val currency: CurrencyKind,
    @SerialName("total_amount") val totalAmount: Long,
    @SerialName("invoice_payload") val invoicePayload: String,
    @SerialName("telegram_payment_charge_id") val telegramPaymentChargeId: String,
    @SerialName("provider_payment_charge_id") val providerPaymentChargeId: String,
    @SerialName("subscription_expiration_date") val subscriptionExpirationDate: Long? = null,
    @SerialName("is_recurring") val isRecurring: Boolean? = null,
    @SerialName("is_first_recurring") val isFirstRecurring: Boolean? = null,
    @SerialName("shipping_option_id") val shippingOptionId: String? = null,
    @SerialName("order_info") val orderInfo: OrderInfo? = null
)

