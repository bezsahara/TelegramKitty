package org.bezsahara.kittybot.telegram.classes.backgrounds


import org.bezsahara.kittybot.telegram.classes.media.Document
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * The background is a PNG or TGV (gzipped subset of SVG with MIME type "application/x-tgwallpattern") pattern to be combined with the background fill chosen by the user.
 * 
 * *[link](https://core.telegram.org/bots/api#backgroundtypepattern)*: https://core.telegram.org/bots/api#backgroundtypepattern
 * 
 * @param type Type of the background, always "pattern"
 * @param document Document with the pattern
 * @param fill The background fill that is combined with the pattern
 * @param intensity Intensity of the pattern when it is shown above the filled background; 0-100
 * @param isInverted Optional. True, if the background fill must be applied only to the pattern itself. All other pixels are black in this case. For dark themes only
 * @param isMoving Optional. True, if the background moves slightly when the device is tilted
 */
@Serializable
data class BackgroundTypePattern(
    val document: Document,
    val fill: BackgroundFill,
    val intensity: Long,
    @SerialName("is_inverted") val isInverted: Boolean? = null,
    @SerialName("is_moving") val isMoving: Boolean? = null,
) : BackgroundType {
    override val type: String = "pattern"
}

