package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * The background is a gradient fill.
 * 
 * *[link](https://core.telegram.org/bots/api#backgroundfillgradient)*: https://core.telegram.org/bots/api#backgroundfillgradient
 * 
 * @param type Type of the background fill, always "gradient"
 * @param topColor Top color of the gradient in the RGB24 format
 * @param bottomColor Bottom color of the gradient in the RGB24 format
 * @param rotationAngle Clockwise rotation angle of the background fill in degrees; 0-359
 */
@Serializable
data class BackgroundFillGradient(
    @SerialName("top_color") val topColor: Long,
    @SerialName("bottom_color") val bottomColor: Long,
    @SerialName("rotation_angle") val rotationAngle: Long,
) : BackgroundFill {
    override val type: String = "gradient"
}

