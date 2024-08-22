package org.bezsahara.kittybot.telegram.classes.permissions


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * This object represents a service message about a user allowing a bot to write messages after adding it to the attachment menu, launching a Web App from a link, or accepting an explicit request from a Web App sent by the method requestWriteAccess.
 * 
 * *[link](https://core.telegram.org/bots/api#writeaccessallowed)*: https://core.telegram.org/bots/api#writeaccessallowed
 * 
 * @param fromRequest Optional. True, if the access was granted after the user accepted an explicit request from a Web App sent by the method requestWriteAccess
 * @param webAppName Optional. Name of the Web App, if the access was granted when the Web App was launched from a link
 * @param fromAttachmentMenu Optional. True, if the access was granted when the bot was added to the attachment or side menu
 */
@Serializable
data class WriteAccessAllowed(
    @SerialName("from_request") val fromRequest: Boolean? = null,
    @SerialName("web_app_name") val webAppName: String? = null,
    @SerialName("from_attachment_menu") val fromAttachmentMenu: Boolean? = null
)

