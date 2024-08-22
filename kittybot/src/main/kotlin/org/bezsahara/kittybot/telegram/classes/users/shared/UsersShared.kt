package org.bezsahara.kittybot.telegram.classes.users.shared


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object contains information about the users whose identifiers were shared with the bot using a KeyboardButtonRequestUsers button.
 * 
 * *[link](https://core.telegram.org/bots/api#usersshared)*: https://core.telegram.org/bots/api#usersshared
 * 
 * @param requestId Identifier of the request
 * @param users Information about users shared with the bot.
 */
@Serializable
data class UsersShared(
    @SerialName("request_id") val requestId: Long,
    val users: List<SharedUser>
)

