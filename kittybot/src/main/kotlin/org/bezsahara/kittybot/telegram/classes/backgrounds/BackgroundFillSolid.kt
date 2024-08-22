package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName



/**
 * The background is filled using the selected color.
 * 
 * *[link](https://core.telegram.org/bots/api#backgroundfillsolid)*: https://core.telegram.org/bots/api#backgroundfillsolid
 * 
 * @param type Type of the background fill, always "solid"
 * @param color The color of the background fill in the RGB24 format
 */
@Serializable
data class BackgroundFillSolid(
    val color: Long,
) : BackgroundFill {
    override val type: String = "solid"
}

