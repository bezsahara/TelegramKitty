package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * The background is automatically filled based on the selected colors.
 * 
 * *[link](https://core.telegram.org/bots/api#backgroundtypefill)*: https://core.telegram.org/bots/api#backgroundtypefill
 * 
 * @param type Type of the background, always "fill"
 * @param fill The background fill
 * @param darkThemeDimming Dimming of the background in dark themes, as a percentage; 0-100
 */
@Serializable
data class BackgroundTypeFill(
    val fill: BackgroundFill,
    @SerialName("dark_theme_dimming") val darkThemeDimming: Long,
) : BackgroundType {
    override val type: String = "fill"
}

