package org.bezsahara.kittybot.telegram.classes.media

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.media.PaidMedia


/**
 * The paid media isn't available before the payment.
 * 
 * [link](https://core.telegram.org/bots/api#paidmediapreview): https://core.telegram.org/bots/api#paidmediapreview
 * 
 * @param type Type of the paid media, always "preview"
 * @param width Optional. Media width as defined by the sender
 * @param height Optional. Media height as defined by the sender
 * @param duration Optional. Duration of the media in seconds as defined by the sender
 */
@Serializable
data class PaidMediaPreview(
    val width: Long? = null,
    val height: Long? = null,
    val duration: Long? = null
) : PaidMedia {
    override val type: String = "preview"
}

