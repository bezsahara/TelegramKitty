package org.bezsahara.kittybot.telegram.classes.stickers


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object describes the position on faces where a mask should be placed by default.
 * 
 * *[link](https://core.telegram.org/bots/api#maskposition)*: https://core.telegram.org/bots/api#maskposition
 * 
 * @param point The part of the face relative to which the mask should be placed. One of "forehead", "eyes", "mouth", or "chin".
 * @param xShift Shift by X-axis measured in widths of the mask scaled to the face size, from left to right. For example, choosing -1.0 will place mask just to the left of the default mask position.
 * @param yShift Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom. For example, 1.0 will place the mask just below the default mask position.
 * @param scale Mask scaling coefficient. For example, 2.0 means double size.
 */
@Serializable
data class MaskPosition(
    val point: String,
    @SerialName("x_shift") val xShift: Float,
    @SerialName("y_shift") val yShift: Float,
    val scale: Float
)

