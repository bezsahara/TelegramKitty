package org.bezsahara.kittybot.telegram.classes.gifts

import org.bezsahara.kittybot.telegram.classes.gifts.Gift
import kotlinx.serialization.SerialName
import kotlin.collections.List
import kotlinx.serialization.Serializable


/**
 * This object represent a list of gifts.
 * 
 * [link](https://core.telegram.org/bots/api#gifts): https://core.telegram.org/bots/api#gifts
 * 
 * @param gifts The list of gifts
 */
@Serializable
data class Gifts(
    val gifts: List<Gift>
)

