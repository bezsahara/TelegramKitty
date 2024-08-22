package org.bezsahara.kittybot.telegram.classes.users.details


import kotlinx.serialization.Serializable


/**
 * Describes the birthdate of a user.
 * 
 * *[link](https://core.telegram.org/bots/api#birthdate)*: https://core.telegram.org/bots/api#birthdate
 * 
 * @param day Day of the user's birth; 1-31
 * @param month Month of the user's birth; 1-12
 * @param year Optional. Year of the user's birth
 */
@Serializable
data class Birthdate(
    val day: Long,
    val month: Long,
    val year: Long? = null
)

