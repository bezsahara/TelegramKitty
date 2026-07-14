package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Formatted date and time.
 *
 * [link](https://core.telegram.org/bots/api#richtextdatetime): https://core.telegram.org/bots/api#richtextdatetime
 *
 * @param type Type of the rich text, always "date_time"
 * @param text The text
 * @param unixTime The Unix time associated with the entity
 * @param dateTimeFormat The string that defines the formatting of the date and time. See date-time entity formatting for more details.
 */
@Serializable
data class RichTextDateTime(
    val text: RichText,
    @SerialName("unix_time") val unixTime: Long,
    @SerialName("date_time_format") val dateTimeFormat: String
) : RichText {
    override val type: String = "date_time"
}
