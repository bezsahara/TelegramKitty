package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.Serializable


/**
 * Describes a clickable area on a story media.
 * 
 * [link](https://core.telegram.org/bots/api#storyarea): https://core.telegram.org/bots/api#storyarea
 * 
 * @param position Position of the area
 * @param type Type of the area
 */
@Serializable
data class StoryArea(
    val position: StoryAreaPosition,
    val type: StoryAreaType
)

