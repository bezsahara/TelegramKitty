package org.bezsahara.kittybot.telegram.classes.gifts

import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftSymbol
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftBackdrop
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftModel
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat


/**
 * This object describes a unique gift that was upgraded from a regular gift.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegift): https://core.telegram.org/bots/api#uniquegift
 * 
 * @param baseName Human-readable name of the regular gift from which this unique gift was upgraded
 * @param name Unique name of the gift. This name can be used in https://t.me/nft/... links and story areas
 * @param number Unique number of the upgraded gift among gifts upgraded from the same regular gift
 * @param model Model of the gift
 * @param symbol Symbol of the gift
 * @param backdrop Backdrop of the gift
 * @param publisherChat Optional. Information about the chat that published the gift
 */
@Serializable
data class UniqueGift(
    @SerialName("base_name") val baseName: String,
    val name: String,
    val number: Long,
    val model: UniqueGiftModel,
    val symbol: UniqueGiftSymbol,
    val backdrop: UniqueGiftBackdrop,
    @SerialName("publisher_chat") val publisherChat: Chat? = null
)

