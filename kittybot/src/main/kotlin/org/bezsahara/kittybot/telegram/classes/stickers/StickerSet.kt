package org.bezsahara.kittybot.telegram.classes.stickers


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.photos.PhotoSize


/**
 * This object represents a sticker set.
 * 
 * *[link](https://core.telegram.org/bots/api#stickerset)*: https://core.telegram.org/bots/api#stickerset
 * 
 * @param name Sticker set name
 * @param title Sticker set title
 * @param stickerType Type of stickers in the set, currently one of "regular", "mask", "custom_emoji"
 * @param stickers List of all set stickers
 * @param thumbnail Optional. Sticker set thumbnail in the .WEBP, .TGS, or .WEBM format
 */
@Serializable
data class StickerSet(
    val name: String,
    val title: String,
    @SerialName("sticker_type") val stickerType: String,
    val stickers: List<Sticker>,
    val thumbnail: PhotoSize? = null
)

