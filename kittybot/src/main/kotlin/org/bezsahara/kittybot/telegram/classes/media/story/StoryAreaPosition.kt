package org.bezsahara.kittybot.telegram.classes.media.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes the position of a clickable area within a story.
 * 
 * [link](https://core.telegram.org/bots/api#storyareaposition): https://core.telegram.org/bots/api#storyareaposition
 * 
 * @param xPercentage The abscissa of the area's center, as a percentage of the media width
 * @param yPercentage The ordinate of the area's center, as a percentage of the media height
 * @param widthPercentage The width of the area's rectangle, as a percentage of the media width
 * @param heightPercentage The height of the area's rectangle, as a percentage of the media height
 * @param rotationAngle The clockwise rotation angle of the rectangle, in degrees; 0-360
 * @param cornerRadiusPercentage The radius of the rectangle corner rounding, as a percentage of the media width
 */
@Serializable
data class StoryAreaPosition(
    @SerialName("x_percentage") val xPercentage: Double,
    @SerialName("y_percentage") val yPercentage: Double,
    @SerialName("width_percentage") val widthPercentage: Double,
    @SerialName("height_percentage") val heightPercentage: Double,
    @SerialName("rotation_angle") val rotationAngle: Double,
    @SerialName("corner_radius_percentage") val cornerRadiusPercentage: Double
)

