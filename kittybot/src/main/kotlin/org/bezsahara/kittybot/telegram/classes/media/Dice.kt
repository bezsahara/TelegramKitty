package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents an animated emoji that displays a random value.
 * 
 * [link](https://core.telegram.org/bots/api#dice): https://core.telegram.org/bots/api#dice
 * 
 * @param emoji Emoji on which the dice throw animation is based
 * @param value Value of the dice, 1-6 for "🎲", "🎯" and "🎳" base emoji, 1-5 for "🏀" and "⚽" base emoji, 1-64 for "🎰" base emoji
 */
@Serializable
data class Dice(
    val emoji: String,
    val value: Long
)

