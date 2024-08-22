package org.bezsahara.kittybot.telegram.classes.webapps


import kotlinx.serialization.Serializable


/**
 * Describes a Web App.
 * 
 * *[link](https://core.telegram.org/bots/api#webappinfo)*: https://core.telegram.org/bots/api#webappinfo
 * 
 * @param url An HTTPS URL of a Web App to be opened with additional data as specified in Initializing Web Apps
 */
@Serializable
data class WebAppInfo(
    val url: String
)

