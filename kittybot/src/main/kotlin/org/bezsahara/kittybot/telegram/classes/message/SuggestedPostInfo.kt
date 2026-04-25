package org.bezsahara.kittybot.telegram.classes.message

import org.bezsahara.kittybot.telegram.values.SuggestedPostState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostPrice


/**
 * Contains information about a suggested post.
 * 
 * [link](https://core.telegram.org/bots/api#suggestedpostinfo): https://core.telegram.org/bots/api#suggestedpostinfo
 * 
 * @param state State of the suggested post. Currently, it can be one of "pending", "approved", "declined".
 * @param price Optional. Proposed price of the post. If the field is omitted, then the post is unpaid.
 * @param sendDate Optional. Proposed send date of the post. If the field is omitted, then the post can be published at any time within 30 days at the sole discretion of the user or administrator who approves it.
 */
@Serializable
data class SuggestedPostInfo(
    val state: SuggestedPostState,
    val price: SuggestedPostPrice? = null,
    @SerialName("send_date") val sendDate: Long? = null
)

