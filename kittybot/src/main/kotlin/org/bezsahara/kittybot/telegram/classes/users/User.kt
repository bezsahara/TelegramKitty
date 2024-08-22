package org.bezsahara.kittybot.telegram.classes.users


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a Telegram user or bot.
 * 
 * *[link](https://core.telegram.org/bots/api#user)*: https://core.telegram.org/bots/api#user
 * 
 * @param id Unique identifier for this user or bot. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.
 * @param isBot True, if this user is a bot
 * @param firstName User's or bot's first name
 * @param lastName Optional. User's or bot's last name
 * @param username Optional. User's or bot's username
 * @param languageCode Optional. IETF language tag of the user's language
 * @param isPremium Optional. True, if this user is a Telegram Premium user
 * @param addedToAttachmentMenu Optional. True, if this user added the bot to the attachment menu
 * @param canJoinGroups Optional. True, if the bot can be invited to groups. Returned only in getMe.
 * @param canReadAllGroupMessages Optional. True, if privacy mode is disabled for the bot. Returned only in getMe.
 * @param supportsInlineQueries Optional. True, if the bot supports inline queries. Returned only in getMe.
 * @param canConnectToBusiness Optional. True, if the bot can be connected to a Telegram Business account to receive its messages. Returned only in getMe.
 */
@Serializable
data class User(
    val id: Long,
    @SerialName("is_bot") val isBot: Boolean,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String? = null,
    val username: String? = null,
    @SerialName("language_code") val languageCode: String? = null,
    @SerialName("is_premium") val isPremium: Boolean? = null,
    @SerialName("added_to_attachment_menu") val addedToAttachmentMenu: Boolean? = null,
    @SerialName("can_join_groups") val canJoinGroups: Boolean? = null,
    @SerialName("can_read_all_group_messages") val canReadAllGroupMessages: Boolean? = null,
    @SerialName("supports_inline_queries") val supportsInlineQueries: Boolean? = null,
    @SerialName("can_connect_to_business") val canConnectToBusiness: Boolean? = null
)

