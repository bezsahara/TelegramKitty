package org.bezsahara.kittybot.telegram.classes.business


import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.stickers.Sticker


/**
 * Contains information about the start page settings of a Telegram Business account.
 * 
 * *[link](https://core.telegram.org/bots/api#businessintro)*: https://core.telegram.org/bots/api#businessintro
 * 
 * @param title Optional. Title text of the business intro
 * @param message Optional. Message text of the business intro
 * @param sticker Optional. Sticker of the business intro
 */
@Serializable
data class BusinessIntro(
    val title: String? = null,
    val message: String? = null,
    val sticker: Sticker? = null
)

