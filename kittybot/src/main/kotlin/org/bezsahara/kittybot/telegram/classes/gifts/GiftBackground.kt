package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object describes the background of a gift.
 * 
 * [link](https://core.telegram.org/bots/api#giftbackground): https://core.telegram.org/bots/api#giftbackground
 * 
 * @param centerColor Center color of the background in RGB format
 * @param edgeColor Edge color of the background in RGB format
 * @param textColor Text color of the background in RGB format
 */
@Serializable
data class GiftBackground(
    @SerialName("center_color") val centerColor: Long,
    @SerialName("edge_color") val edgeColor: Long,
    @SerialName("text_color") val textColor: Long
)

