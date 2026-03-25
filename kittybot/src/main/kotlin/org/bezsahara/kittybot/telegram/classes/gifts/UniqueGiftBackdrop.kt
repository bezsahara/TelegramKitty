package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftBackdropColors
import kotlinx.serialization.Serializable


/**
 * This object describes the backdrop of a unique gift.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegiftbackdrop): https://core.telegram.org/bots/api#uniquegiftbackdrop
 * 
 * @param name Name of the backdrop
 * @param colors Colors of the backdrop
 * @param rarityPerMille The number of unique gifts that receive this backdrop for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftBackdrop(
    val name: String,
    val colors: UniqueGiftBackdropColors,
    @SerialName("rarity_per_mille") val rarityPerMille: Long
)

