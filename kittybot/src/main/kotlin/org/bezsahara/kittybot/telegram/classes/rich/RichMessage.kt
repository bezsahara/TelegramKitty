package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Rich formatted message.
 *
 * [link](https://core.telegram.org/bots/api#richmessage): https://core.telegram.org/bots/api#richmessage
 *
 * @param blocks Content of the message
 * @param isRtl Optional. True, if the rich message must be shown right-to-left
 */
@Serializable
data class RichMessage(
    val blocks: List<RichBlock>,
    @SerialName("is_rtl") val isRtl: Boolean? = null
)
