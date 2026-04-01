package org.bezsahara.kittybot.telegram.classes.message.reactions

import kotlinx.serialization.json.buildJsonObject
import org.bezsahara.kittybot.bot.json.PureJsonSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonPrimitive
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionTypePaid


/**
 * The reaction is paid.
 * 
 * [link](https://core.telegram.org/bots/api#reactiontypepaid): https://core.telegram.org/bots/api#reactiontypepaid
 * 
 * @param type Type of the reaction, always "paid"
 */
@Serializable(with = ReactionTypePaidJsonSerializer::class)
object ReactionTypePaid : ReactionType {
    override val type: String get() = "paid"
}


internal class ReactionTypePaidJsonSerializer : PureJsonSerializer<ReactionTypePaid>("ReactionTypePaid", ReactionTypePaid, buildJsonObject { put("type", JsonPrimitive("paid")) })



