package org.bezsahara.kittybot.telegram.classes.media.stickers

import kotlinx.serialization.SerialName
import kotlin.collections.List
import org.bezsahara.kittybot.telegram.values.StickerType
import org.bezsahara.kittybot.telegram.classes.media.PhotoSize
import org.bezsahara.kittybot.telegram.classes.media.stickers.Sticker
import kotlinx.serialization.Serializable


/**
 * This object represents a sticker set.
 * 
 * [link](https://core.telegram.org/bots/api#stickerset): https://core.telegram.org/bots/api#stickerset
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
    @SerialName("sticker_type") val stickerType: StickerType,
    val stickers: List<Sticker>,
    val thumbnail: PhotoSize? = null
)

