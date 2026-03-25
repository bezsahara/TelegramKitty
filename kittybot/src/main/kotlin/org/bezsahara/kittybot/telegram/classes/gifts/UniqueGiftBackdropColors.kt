package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object describes the colors of the backdrop of a unique gift.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegiftbackdropcolors): https://core.telegram.org/bots/api#uniquegiftbackdropcolors
 * 
 * @param centerColor The color in the center of the backdrop in RGB format
 * @param edgeColor The color on the edges of the backdrop in RGB format
 * @param symbolColor The color to be applied to the symbol in RGB format
 * @param textColor The color for the text on the backdrop in RGB format
 */
@Serializable
data class UniqueGiftBackdropColors(
    @SerialName("center_color") val centerColor: Long,
    @SerialName("edge_color") val edgeColor: Long,
    @SerialName("symbol_color") val symbolColor: Long,
    @SerialName("text_color") val textColor: Long
)

