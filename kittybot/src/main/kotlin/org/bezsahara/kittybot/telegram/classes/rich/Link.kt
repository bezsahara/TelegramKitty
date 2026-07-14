package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * Represents an HTTP link.
 *
 * [link](https://core.telegram.org/bots/api#link): https://core.telegram.org/bots/api#link
 *
 * @param url URL of the link
 */
@Serializable
data class Link(
    val url: String
)
