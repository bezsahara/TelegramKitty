package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.Document


/**
 * The background is a wallpaper in the JPEG format.
 * 
 * *[link](https://core.telegram.org/bots/api#backgroundtypewallpaper)*: https://core.telegram.org/bots/api#backgroundtypewallpaper
 * 
 * @param type Type of the background, always "wallpaper"
 * @param document Document with the wallpaper
 * @param darkThemeDimming Dimming of the background in dark themes, as a percentage; 0-100
 * @param isBlurred Optional. True, if the wallpaper is downscaled to fit in a 450x450 square and then box-blurred with radius 12
 * @param isMoving Optional. True, if the background moves slightly when the device is tilted
 */
@Serializable
data class BackgroundTypeWallpaper(
    val document: Document,
    @SerialName("dark_theme_dimming") val darkThemeDimming: Long,
    @SerialName("is_blurred") val isBlurred: Boolean? = null,
    @SerialName("is_moving") val isMoving: Boolean? = null,
) : BackgroundType {
    override val type: String = "wallpaper"
}

