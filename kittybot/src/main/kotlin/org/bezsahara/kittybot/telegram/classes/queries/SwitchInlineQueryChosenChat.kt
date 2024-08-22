package org.bezsahara.kittybot.telegram.classes.queries


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents an inline button that switches the current user to inline mode in a chosen chat, with an optional default inline query.
 * 
 * *[link](https://core.telegram.org/bots/api#switchinlinequerychosenchat)*: https://core.telegram.org/bots/api#switchinlinequerychosenchat
 * 
 * @param query Optional. The default inline query to be inserted in the input field. If left empty, only the bot's username will be inserted
 * @param allowUserChats Optional. True, if private chats with users can be chosen
 * @param allowBotChats Optional. True, if private chats with bots can be chosen
 * @param allowGroupChats Optional. True, if group and supergroup chats can be chosen
 * @param allowChannelChats Optional. True, if channel chats can be chosen
 */
@Serializable
data class SwitchInlineQueryChosenChat(
    val query: String? = null,
    @SerialName("allow_user_chats") val allowUserChats: Boolean? = null,
    @SerialName("allow_bot_chats") val allowBotChats: Boolean? = null,
    @SerialName("allow_group_chats") val allowGroupChats: Boolean? = null,
    @SerialName("allow_channel_chats") val allowChannelChats: Boolean? = null
)

