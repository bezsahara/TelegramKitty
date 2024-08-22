package org.bezsahara.kittybot.telegram.classes.games


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.Animation
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity


/**
 * This object represents a game. Use BotFather to create and edit games, their short names will act as unique identifiers.
 * 
 * *[link](https://core.telegram.org/bots/api#game)*: https://core.telegram.org/bots/api#game
 * 
 * @param title Title of the game
 * @param description Description of the game
 * @param photo Photo that will be displayed in the game message in chats.
 * @param text Optional. Brief description of the game or high scores included in the game message. Can be automatically edited to include current high scores for the game when the bot calls setGameScore, or manually edited using editMessageText. 0-4096 characters.
 * @param textEntities Optional. Special entities that appear in text, such as usernames, URLs, bot commands, etc.
 * @param animation Optional. Animation that will be displayed in the game message in chats. Upload via BotFather
 */
@Serializable
data class Game(
    val title: String,
    val description: String,
    val photo: List<PhotoSize>,
    val text: String? = null,
    @SerialName("text_entities") val textEntities: List<MessageEntity>? = null,
    val animation: Animation? = null
)

