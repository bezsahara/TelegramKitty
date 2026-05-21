package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostPrice


/**
 * Describes a service message about the failed approval of a suggested post. Currently, only caused by insufficient user funds at the time of approval.
 * 
 * [link](https://core.telegram.org/bots/api#suggestedpostapprovalfailed): https://core.telegram.org/bots/api#suggestedpostapprovalfailed
 * 
 * @param suggestedPostMessage Optional. Message containing the suggested post whose approval has failed. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param price Expected price of the post
 */
@Serializable
data class SuggestedPostApprovalFailed(
    val price: SuggestedPostPrice,
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null
)

