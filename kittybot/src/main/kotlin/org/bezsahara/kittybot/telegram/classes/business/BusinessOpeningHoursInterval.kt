package org.bezsahara.kittybot.telegram.classes.business


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes an interval of time during which a business is open.
 * 
 * *[link](https://core.telegram.org/bots/api#businessopeninghoursinterval)*: https://core.telegram.org/bots/api#businessopeninghoursinterval
 * 
 * @param openingMinute The minute's sequence number in a week, starting on Monday, marking the start of the time interval during which the business is open; 0 - 7 * 24 * 60
 * @param closingMinute The minute's sequence number in a week, starting on Monday, marking the end of the time interval during which the business is open; 0 - 8 * 24 * 60
 */
@Serializable
data class BusinessOpeningHoursInterval(
    @SerialName("opening_minute") val openingMinute: Long,
    @SerialName("closing_minute") val closingMinute: Long
)

