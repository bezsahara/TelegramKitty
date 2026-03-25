package org.bezsahara.kittybot.telegram.classes.message.service

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.message.Message
import kotlinx.serialization.Serializable


/**
 * Describes a service message about the rejection of a suggested post.
 * 
 * [link](https://core.telegram.org/bots/api#suggestedpostdeclined): https://core.telegram.org/bots/api#suggestedpostdeclined
 * 
 * @param suggestedPostMessage Optional. Message containing the suggested post. Note that the Message object in this field will not contain the reply_to_message field even if it itself is a reply.
 * @param comment Optional. Comment with which the post was declined
 */
@Serializable
data class SuggestedPostDeclined(
    @SerialName("suggested_post_message") val suggestedPostMessage: Message? = null,
    val comment: String? = null
)

