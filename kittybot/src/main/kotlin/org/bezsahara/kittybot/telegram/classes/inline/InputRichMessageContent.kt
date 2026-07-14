package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.rich.InputRichMessage


/**
 * Represents the content of a rich message to be sent as the result of an inline query.
 *
 * [link](https://core.telegram.org/bots/api#inputrichmessagecontent): https://core.telegram.org/bots/api#inputrichmessagecontent
 *
 * @param richMessage The message to be sent
 */
@Serializable
data class InputRichMessageContent(
    @SerialName("rich_message") val richMessage: InputRichMessage
) : InputMessageContent
