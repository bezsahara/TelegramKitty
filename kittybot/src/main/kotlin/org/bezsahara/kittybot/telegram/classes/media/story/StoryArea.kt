package org.bezsahara.kittybot.telegram.classes.media.story

import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaPosition
import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaType
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

