package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.story.StoryAreaType
import org.bezsahara.kittybot.telegram.classes.media.story.LocationAddress
import kotlinx.serialization.Serializable


/**
 * Describes a story area pointing to a location. Currently, a story can have up to 10 location areas.
 * 
 * [link](https://core.telegram.org/bots/api#storyareatypelocation): https://core.telegram.org/bots/api#storyareatypelocation
 * 
 * @param type Type of the area, always "location"
 * @param latitude Location latitude in degrees
 * @param longitude Location longitude in degrees
 * @param address Optional. Address of the location
 */
@Serializable
data class StoryAreaTypeLocation(
    val latitude: Double,
    val longitude: Double,
    val address: LocationAddress? = null
) : StoryAreaType {
    override val type: String get() = "location"
}

