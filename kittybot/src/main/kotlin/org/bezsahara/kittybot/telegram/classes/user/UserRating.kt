package org.bezsahara.kittybot.telegram.classes.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object describes the rating of a user based on their Telegram Star spendings.
 * 
 * [link](https://core.telegram.org/bots/api#userrating): https://core.telegram.org/bots/api#userrating
 * 
 * @param level Current level of the user, indicating their reliability when purchasing digital goods and services. A higher level suggests a more trustworthy customer; a negative level is likely reason for concern.
 * @param rating Numerical value of the user's rating; the higher the rating, the better
 * @param currentLevelRating The rating value required to get the current level
 * @param nextLevelRating Optional. The rating value required to get to the next level; omitted if the maximum level was reached
 */
@Serializable
data class UserRating(
    val level: Long,
    val rating: Long,
    @SerialName("current_level_rating") val currentLevelRating: Long,
    @SerialName("next_level_rating") val nextLevelRating: Long? = null
)

