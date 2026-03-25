package org.bezsahara.kittybot.telegram.classes.chat

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import kotlinx.serialization.Serializable


/**
 * Represents a location to which a chat is connected.
 * 
 * [link](https://core.telegram.org/bots/api#chatlocation): https://core.telegram.org/bots/api#chatlocation
 * 
 * @param location The location to which the supergroup is connected. Can't be a live location.
 * @param address Location address; 1-64 characters, as defined by the chat owner
 */
@Serializable
data class ChatLocation(
    val location: Location,
    val address: String
)

