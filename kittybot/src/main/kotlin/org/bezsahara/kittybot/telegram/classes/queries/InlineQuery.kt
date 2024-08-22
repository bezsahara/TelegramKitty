package org.bezsahara.kittybot.telegram.classes.queries


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.locations.Location
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents an incoming inline query. When the user sends an empty query, your bot could return some default or trending results.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequery)*: https://core.telegram.org/bots/api#inlinequery
 * 
 * @param id Unique identifier for this query
 * @param from Sender
 * @param query Text of the query (up to 256 characters)
 * @param offset Offset of the results to be returned, can be controlled by the bot
 * @param chatType Optional. Type of the chat from which the inline query was sent. Can be either "sender" for a private chat with the inline query sender, "private", "group", "supergroup", or "channel". The chat type should be always known for requests sent from official clients and most third-party clients, unless the request was sent from a secret chat
 * @param location Optional. Sender location, only for bots that request user location
 */
@Serializable
data class InlineQuery(
    val id: String,
    val from: User,
    val query: String,
    val offset: String,
    @SerialName("chat_type") val chatType: String? = null,
    val location: Location? = null
)

