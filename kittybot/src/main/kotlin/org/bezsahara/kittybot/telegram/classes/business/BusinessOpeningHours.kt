package org.bezsahara.kittybot.telegram.classes.business


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes the opening hours of a business.
 * 
 * *[link](https://core.telegram.org/bots/api#businessopeninghours)*: https://core.telegram.org/bots/api#businessopeninghours
 * 
 * @param timeZoneName Unique name of the time zone for which the opening hours are defined
 * @param openingHours List of time intervals describing business opening hours
 */
@Serializable
data class BusinessOpeningHours(
    @SerialName("time_zone_name") val timeZoneName: String,
    @SerialName("opening_hours") val openingHours: List<BusinessOpeningHoursInterval>
)

