package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.Message
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostPrice


/**
 * Describes a service message about the approval of a suggested post.
 * 
 * [link](https://core.telegram.org/bots/api#suggestedpostapproved): https://core.telegram.org/bots/api#suggestedpostapproved
 * 
 * @param suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param price Optional. Amount paid for the post
 * @param sendDate Date when the post will be published
 */
@Serializable
data class SuggestedPostApproved(
    @SerialName("send_date") val sendDate: Long,
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null,
    val price: SuggestedPostPrice? = null
)

