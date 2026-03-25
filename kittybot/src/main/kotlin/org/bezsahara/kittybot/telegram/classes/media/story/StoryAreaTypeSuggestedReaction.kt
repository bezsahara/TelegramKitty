package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaType
import org.bezsahara.kittybot.telegram.classes.message.reactions.ReactionType
import kotlinx.serialization.Serializable


/**
 * Describes a story area pointing to a suggested reaction. Currently, a story can have up to 5 suggested reaction areas.
 * 
 * [link](https://core.telegram.org/bots/api#storyareatypesuggestedreaction): https://core.telegram.org/bots/api#storyareatypesuggestedreaction
 * 
 * @param type Type of the area, always "suggested_reaction"
 * @param reactionType Type of the reaction
 * @param isDark Optional. Pass True if the reaction area has a dark background
 * @param isFlipped Optional. Pass True if reaction area corner is flipped
 */
@Serializable
data class StoryAreaTypeSuggestedReaction(
    @SerialName("reaction_type") val reactionType: ReactionType,
    @SerialName("is_dark") val isDark: Boolean? = null,
    @SerialName("is_flipped") val isFlipped: Boolean? = null
) : StoryAreaType {
    override val type: String get() = "suggested_reaction"
}

