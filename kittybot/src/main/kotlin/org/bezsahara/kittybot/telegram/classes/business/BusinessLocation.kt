package org.bezsahara.kittybot.telegram.classes.business

import kotlinx.serialization.SerialName
import org.bezsahara.kittybot.telegram.classes.media.geo.Location
import kotlinx.serialization.Serializable


/**
 * Contains information about the location of a Telegram Business account.
 * 
 * [link](https://core.telegram.org/bots/api#businesslocation): https://core.telegram.org/bots/api#businesslocation
 * 
 * @param address Address of the business
 * @param location Optional. Location of the business
 */
@Serializable
data class BusinessLocation(
    val address: String,
    val location: Location? = null
)

