package org.bezsahara.kittybot.telegram.classes.backgrounds


import kotlinx.serialization.Serializable


/**
 * The background is a freeform gradient that rotates after every message in the chat.
 * 
 * *[link](https://core.telegram.org/bots/api#backgroundfillfreeformgradient)*: https://core.telegram.org/bots/api#backgroundfillfreeformgradient
 * 
 * @param type Type of the background fill, always "freeform_gradient"
 * @param colors A list of the 3 or 4 base colors that are used to generate the freeform gradient in the RGB24 format
 */
@Serializable
data class BackgroundFillFreeformGradient(
    val colors: List<Long>,
) : BackgroundFill {
    override val type: String = "freeform_gradient"
}

