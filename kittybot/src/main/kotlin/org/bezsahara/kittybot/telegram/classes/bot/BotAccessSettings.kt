package org.bezsahara.kittybot.telegram.classes.bot

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * This object describes the access settings of a bot.
 * 
 * [link](https://core.telegram.org/bots/api#botaccesssettings): https://core.telegram.org/bots/api#botaccesssettings
 * 
 * @param isAccessRestricted True, if only selected users can access the bot. The bot's owner can always access it.
 * @param addedUsers Optional. The list of other users who have access to the bot if the access is restricted
 */
@Serializable
data class BotAccessSettings(
    @SerialName("is_access_restricted") val isAccessRestricted: Boolean,
    @SerialName("added_users") val addedUsers: List<User>? = null
)

