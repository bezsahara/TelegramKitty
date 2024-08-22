package org.bezsahara.kittybot.telegram.classes.queries.results


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.keyboards.InlineKeyboardMarkup


/**
 * Represents a Game.
 * 
 * *[link](https://core.telegram.org/bots/api#inlinequeryresultgame)*: https://core.telegram.org/bots/api#inlinequeryresultgame
 * 
 * @param type Type of the result, must be game
 * @param id Unique identifier for this result, 1-64 bytes
 * @param gameShortName Short name of the game
 * @param replyMarkup Optional. Inline keyboard attached to the message
 */
@Serializable
data class InlineQueryResultGame(
    val id: String,
    @SerialName("game_short_name") val gameShortName: String,
    @SerialName("reply_markup") val replyMarkup: InlineKeyboardMarkup? = null,
) : InlineQueryResult {
    override val type: String = "game"
}

