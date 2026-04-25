package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaType
import org.bezsahara.kittybot.telegram.values.StoryAreaTypeKind
import kotlinx.serialization.Serializable


/**
 * Describes a story area pointing to a unique gift. Currently, a story can have at most 1 unique gift area.
 * 
 * [link](https://core.telegram.org/bots/api#storyareatypeuniquegift): https://core.telegram.org/bots/api#storyareatypeuniquegift
 * 
 * @param type Type of the area, always "unique_gift"
 * @param name Unique name of the gift
 */
@Serializable
data class StoryAreaTypeUniqueGift(
    val name: String
) : StoryAreaType {
    override val type: StoryAreaTypeKind = StoryAreaTypeKind.UNIQUE_GIFT
}

