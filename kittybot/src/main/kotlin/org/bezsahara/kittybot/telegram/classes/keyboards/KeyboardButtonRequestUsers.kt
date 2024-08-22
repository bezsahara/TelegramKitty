package org.bezsahara.kittybot.telegram.classes.keyboards


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * This object defines the criteria used to request suitable users. Information about the selected users will be shared with the bot when the corresponding button is pressed. More about requesting users: https://core.telegram.org/bots/features#chat-and-user-selection
 * 
 * *[link](https://core.telegram.org/bots/api#keyboardbuttonrequestusers)*: https://core.telegram.org/bots/api#keyboardbuttonrequestusers
 * 
 * @param requestId Signed 32-bit identifier of the request that will be received back in the UsersShared object. Must be unique within the message
 * @param userIsBot Optional. Pass True to request bots, pass False to request regular users. If not specified, no additional restrictions are applied.
 * @param userIsPremium Optional. Pass True to request premium users, pass False to request non-premium users. If not specified, no additional restrictions are applied.
 * @param maxQuantity Optional. The maximum number of users to be selected; 1-10. Defaults to 1.
 * @param requestName Optional. Pass True to request the users' first and last names
 * @param requestUsername Optional. Pass True to request the users' usernames
 * @param requestPhoto Optional. Pass True to request the users' photos
 */
@Serializable
data class KeyboardButtonRequestUsers(
    @SerialName("request_id") val requestId: Long,
    @SerialName("user_is_bot") val userIsBot: Boolean? = null,
    @SerialName("user_is_premium") val userIsPremium: Boolean? = null,
    @SerialName("max_quantity") val maxQuantity: Long? = null,
    @SerialName("request_name") val requestName: Boolean? = null,
    @SerialName("request_username") val requestUsername: Boolean? = null,
    @SerialName("request_photo") val requestPhoto: Boolean? = null
)

