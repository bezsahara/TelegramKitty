package org.bezsahara.kittybot.telegram.classes.message.reactions

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import kotlinx.serialization.Serializable


/**
 * The reaction is paid.
 * 
 * [link](https://core.telegram.org/bots/api#reactiontypepaid): https://core.telegram.org/bots/api#reactiontypepaid
 * 
 * @param type Type of the reaction, always "paid"
 */
@Serializable
open class ReactionTypePaid : ReactionType {
    override val type: String get() = "paid"
    companion object Default : ReactionTypePaid()
}

