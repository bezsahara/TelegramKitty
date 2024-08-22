package org.bezsahara.kittybot.telegram.classes.alerts


import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents the content of a service message, sent whenever a user in the chat triggers a proximity alert set by another user.
 * 
 * *[link](https://core.telegram.org/bots/api#proximityalerttriggered)*: https://core.telegram.org/bots/api#proximityalerttriggered
 * 
 * @param traveler User that triggered the alert
 * @param watcher User that set the alert
 * @param distance The distance between the users
 */
@Serializable
data class ProximityAlertTriggered(
    val traveler: User,
    val watcher: User,
    val distance: Long
)

