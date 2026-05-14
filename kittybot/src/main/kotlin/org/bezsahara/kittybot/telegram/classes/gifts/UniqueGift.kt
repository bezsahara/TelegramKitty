package org.bezsahara.kittybot.telegram.classes.gifts

import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftSymbol
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftBackdrop
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftModel
import org.bezsahara.kittybot.telegram.classes.gifts.UniqueGiftColors
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.chat.Chat


/**
 * This object describes a unique gift that was upgraded from a regular gift.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegift): https://core.telegram.org/bots/api#uniquegift
 * 
 * @param giftId Identifier of the regular gift from which the gift was upgraded
 * @param baseName Human-readable name of the regular gift from which this unique gift was upgraded
 * @param name Unique name of the gift. This name can be used in https://t.me/nft/... links and story areas.
 * @param number Unique number of the upgraded gift among gifts upgraded from the same regular gift
 * @param model Model of the gift
 * @param symbol Symbol of the gift
 * @param backdrop Backdrop of the gift
 * @param isPremium Optional. True, if the original regular gift was exclusively purchaseable by Telegram Premium subscribers
 * @param isBurned Optional. True, if the gift was used to craft another gift and isn't available anymore
 * @param isFromBlockchain Optional. True, if the gift is assigned from the TON blockchain and can't be resold or transferred in Telegram
 * @param colors Optional. The color scheme that can be used by the gift's owner for the chat's name, replies to messages and link previews; for business account gifts and gifts that are currently on sale only
 * @param publisherChat Optional. Information about the chat that published the gift
 */
@Serializable
data class UniqueGift(
    @SerialName("gift_id") val giftId: String,
    @SerialName("base_name") val baseName: String,
    val name: String,
    val number: Long,
    val model: UniqueGiftModel,
    val symbol: UniqueGiftSymbol,
    val backdrop: UniqueGiftBackdrop,
    @SerialName("is_premium") val isPremium: Boolean? = null,
    @SerialName("is_burned") val isBurned: Boolean? = null,
    @SerialName("is_from_blockchain") val isFromBlockchain: Boolean? = null,
    val colors: UniqueGiftColors? = null,
    @SerialName("publisher_chat") val publisherChat: Chat? = null
)

