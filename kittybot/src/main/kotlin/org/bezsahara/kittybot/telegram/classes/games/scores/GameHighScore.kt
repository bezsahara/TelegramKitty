package org.bezsahara.kittybot.telegram.classes.games.scores


import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents one row of the high scores table for a game.
 * 
 * *[link](https://core.telegram.org/bots/api#gamehighscore)*: https://core.telegram.org/bots/api#gamehighscore
 * 
 * @param position Position in high score table for the game
 * @param user User
 * @param score Score
 */
@Serializable
data class GameHighScore(
    val position: Long,
    val user: User,
    val score: Long
)

