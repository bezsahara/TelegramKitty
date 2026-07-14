package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder


/**
 * Represents an HTTP link to be sent.
 *
 * [link](https://core.telegram.org/bots/api#inputmedialink): https://core.telegram.org/bots/api#inputmedialink
 *
 * @param type Type of the media, must be link
 * @param url HTTP URL of the link
 */
@Serializable
data class InputMediaLink(
    val url: String
) : InputPollOptionMedia {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
    }
    override val type: String = "link"
}
