package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes a rich message to be sent. Exactly one of the fields html or markdown must be used.
 *
 * [link](https://core.telegram.org/bots/api#inputrichmessage): https://core.telegram.org/bots/api#inputrichmessage
 *
 * @param html Optional. Content of the rich message to send described using HTML formatting. See rich message formatting options for more details.
 * @param markdown Optional. Content of the rich message to send described using Markdown formatting. See rich message formatting options for more details.
 * @param isRtl Optional. Pass True if the rich message must be shown right-to-left
 * @param skipEntityDetection Optional. Pass True to skip automatic detection of entities (e.g., URLs, email addresses, username mentions, hashtags, cashtags, bot commands, or phone numbers) in the text
 */
@Serializable
data class InputRichMessage(
    val html: String? = null,
    val markdown: String? = null,
    @SerialName("is_rtl") val isRtl: Boolean? = null,
    @SerialName("skip_entity_detection") val skipEntityDetection: Boolean? = null
)
