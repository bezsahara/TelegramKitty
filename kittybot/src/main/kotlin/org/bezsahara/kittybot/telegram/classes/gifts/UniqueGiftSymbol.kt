package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import kotlinx.serialization.Serializable


/**
 * This object describes the symbol shown on the pattern of a unique gift.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegiftsymbol): https://core.telegram.org/bots/api#uniquegiftsymbol
 * 
 * @param name Name of the symbol
 * @param sticker The sticker that represents the unique gift
 * @param rarityPerMille The number of unique gifts that receive this model for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftSymbol(
    val name: String,
    val sticker: Sticker,
    @SerialName("rarity_per_mille") val rarityPerMille: Long
)

