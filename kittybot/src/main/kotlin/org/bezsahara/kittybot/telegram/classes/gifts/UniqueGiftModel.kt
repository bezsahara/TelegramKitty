package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import kotlinx.serialization.Serializable


/**
 * This object describes the model of a unique gift.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegiftmodel): https://core.telegram.org/bots/api#uniquegiftmodel
 * 
 * @param name Name of the model
 * @param sticker The sticker that represents the unique gift
 * @param rarityPerMille The number of unique gifts that receive this model for every 1000 gifts upgraded
 */
@Serializable
data class UniqueGiftModel(
    val name: String,
    val sticker: Sticker,
    @SerialName("rarity_per_mille") val rarityPerMille: Long
)

