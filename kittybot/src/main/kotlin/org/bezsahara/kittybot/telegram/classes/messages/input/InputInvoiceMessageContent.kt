package org.bezsahara.kittybot.telegram.classes.messages.input


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.payments.LabeledPrice


/**
 * Represents the content of an invoice message to be sent as the result of an inline query.
 * 
 * *[link](https://core.telegram.org/bots/api#inputinvoicemessagecontent)*: https://core.telegram.org/bots/api#inputinvoicemessagecontent
 * 
 * @param title Product name, 1-32 characters
 * @param description Product description, 1-255 characters
 * @param payload Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
 * @param providerToken Payment provider token, obtained via @BotFather
 * @param currency Three-letter ISO 4217 currency code, see more on currencies
 * @param prices Price breakdown, a JSON-serialized list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
 * @param maxTipAmount Optional. The maximum accepted amount for tips in the smallest units of the currency (integer, not float/double). For example, for a maximum tip of US$ 1.45 pass max_tip_amount = 145. See the exp parameter in currencies.json, it shows the number of digits past the decimal point for each currency (2 for the majority of currencies). Defaults to 0
 * @param suggestedTipAmounts Optional. A JSON-serialized array of suggested amounts of tip in the smallest units of the currency (integer, not float/double). At most 4 suggested tip amounts can be specified. The suggested tip amounts must be positive, passed in a strictly increased order and must not exceed max_tip_amount.
 * @param providerData Optional. A JSON-serialized object for data about the invoice, which will be shared with the payment provider. A detailed description of the required fields should be provided by the payment provider.
 * @param photoUrl Optional. URL of the product photo for the invoice. Can be a photo of the goods or a marketing image for a service.
 * @param photoSize Optional. Photo size in bytes
 * @param photoWidth Optional. Photo width
 * @param photoHeight Optional. Photo height
 * @param needName Optional. Pass True if you require the user's full name to complete the order
 * @param needPhoneNumber Optional. Pass True if you require the user's phone number to complete the order
 * @param needEmail Optional. Pass True if you require the user's email address to complete the order
 * @param needShippingAddress Optional. Pass True if you require the user's shipping address to complete the order
 * @param sendPhoneNumberToProvider Optional. Pass True if the user's phone number should be sent to provider
 * @param sendEmailToProvider Optional. Pass True if the user's email address should be sent to provider
 * @param isFlexible Optional. Pass True if the final price depends on the shipping method
 */
@Serializable
data class InputInvoiceMessageContent(
    val title: String,
    val description: String,
    val payload: String,
    @SerialName("provider_token") val providerToken: String,
    val currency: String,
    val prices: List<LabeledPrice>,
    @SerialName("max_tip_amount") val maxTipAmount: Long? = null,
    @SerialName("suggested_tip_amounts") val suggestedTipAmounts: List<Long>? = null,
    @SerialName("provider_data") val providerData: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("photo_size") val photoSize: Long? = null,
    @SerialName("photo_width") val photoWidth: Long? = null,
    @SerialName("photo_height") val photoHeight: Long? = null,
    @SerialName("need_name") val needName: Boolean? = null,
    @SerialName("need_phone_number") val needPhoneNumber: Boolean? = null,
    @SerialName("need_email") val needEmail: Boolean? = null,
    @SerialName("need_shipping_address") val needShippingAddress: Boolean? = null,
    @SerialName("send_phone_number_to_provider") val sendPhoneNumberToProvider: Boolean? = null,
    @SerialName("send_email_to_provider") val sendEmailToProvider: Boolean? = null,
    @SerialName("is_flexible") val isFlexible: Boolean? = null
) : InputMessageContent

