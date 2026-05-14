package org.bezsahara.kittybot.telegram.classes.input

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.client.file.MultiPartBuilder
import org.bezsahara.kittybot.telegram.client.file.CustomMPB
import org.bezsahara.kittybot.telegram.classes.input.InputPollOptionMedia
import kotlin.Unit
import org.bezsahara.kittybot.telegram.classes.input.InputPollMedia
import kotlinx.serialization.Serializable


/**
 * Represents a location to be sent.
 * 
 * [link](https://core.telegram.org/bots/api#inputmedialocation): https://core.telegram.org/bots/api#inputmedialocation
 * 
 * @param type Type of the result, must be location
 * @param latitude Latitude of the location
 * @param longitude Longitude of the location
 * @param horizontalAccuracy Optional. The radius of uncertainty for the location, measured in meters; 0-1500
 */
@Serializable
data class InputMediaLocation(
    val latitude: Double,
    val longitude: Double,
    @SerialName("horizontal_accuracy") val horizontalAccuracy: Double? = null
) : InputPollMedia, InputPollOptionMedia {
    override suspend fun executeAll(
        builder: CustomMPB
    ) {
    }
    override suspend fun executeAll(
        builder: MultiPartBuilder
    ) {
    }
    override val type: String = "location"
}

