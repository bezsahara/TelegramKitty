package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes a story area containing weather information. Currently, a story can have up to 3 weather areas.
 * 
 * [link](https://core.telegram.org/bots/api#storyareatypeweather): https://core.telegram.org/bots/api#storyareatypeweather
 * 
 * @param type Type of the area, always "weather"
 * @param temperature Temperature, in degree Celsius
 * @param emoji Emoji representing the weather
 * @param backgroundColor A color of the area background in the ARGB format
 */
@Serializable
data class StoryAreaTypeWeather(
    val temperature: Double,
    val emoji: String,
    @SerialName("background_color") val backgroundColor: Long
) : StoryAreaType {
    override val type: String = "weather"
}

