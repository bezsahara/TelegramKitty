package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.Serializable


/**
 * Describes a story area pointing to an HTTP or tg:// link. Currently, a story can have up to 3 link areas.
 * 
 * [link](https://core.telegram.org/bots/api#storyareatypelink): https://core.telegram.org/bots/api#storyareatypelink
 * 
 * @param type Type of the area, always "link"
 * @param url HTTP or tg:// URL to be opened when the area is clicked
 */
@Serializable
data class StoryAreaTypeLink(
    val url: String
) : StoryAreaType {
    override val type: String = "link"
}

