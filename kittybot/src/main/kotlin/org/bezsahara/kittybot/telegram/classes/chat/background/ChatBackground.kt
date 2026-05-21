package org.bezsahara.kittybot.telegram.classes.chat.background

import kotlinx.serialization.Serializable


/**
 * This object represents a chat background.
 * 
 * [link](https://core.telegram.org/bots/api#chatbackground): https://core.telegram.org/bots/api#chatbackground
 * 
 * @param type Type of the background
 */
@Serializable
data class ChatBackground(
    val type: BackgroundType
)

