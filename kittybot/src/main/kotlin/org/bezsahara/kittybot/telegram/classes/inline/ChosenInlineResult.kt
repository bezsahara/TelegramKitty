package org.bezsahara.kittybot.telegram.classes.inline

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import org.bezsahara.kittybot.telegram.classes.user.User
import kotlinx.serialization.Serializable


/**
 * Represents a result of an inline query that was chosen by the user and sent to their chat partner.
 * Note: It is necessary to enable inline feedback via @BotFather in order to receive these objects in updates.
 * 
 * [link](https://core.telegram.org/bots/api#choseninlineresult): https://core.telegram.org/bots/api#choseninlineresult
 * 
 * @param resultId The unique identifier for the result that was chosen
 * @param from The user that chose the result
 * @param location Optional. Sender location, only for bots that require user location
 * @param inlineMessageId Optional. Identifier of the sent inline message. Available only if there is an inline keyboard attached to the message. Will be also received in callback queries and can be used to edit the message.
 * @param query The query that was used to obtain the result
 */
@Serializable
data class ChosenInlineResult(
    @SerialName("result_id") val resultId: String,
    val from: User,
    val query: String,
    val location: Location? = null,
    @SerialName("inline_message_id") val inlineMessageId: String? = null
)

