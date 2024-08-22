package org.bezsahara.kittybot.telegram.classes.messages


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about a change in auto-delete timer settings.
 * 
 * *[link](https://core.telegram.org/bots/api#messageautodeletetimerchanged)*: https://core.telegram.org/bots/api#messageautodeletetimerchanged
 * 
 * @param messageAutoDeleteTime New auto-delete time for messages in the chat; in seconds
 */
@Serializable
data class MessageAutoDeleteTimerChanged(
    @SerialName("message_auto_delete_time") val messageAutoDeleteTime: Long
)

