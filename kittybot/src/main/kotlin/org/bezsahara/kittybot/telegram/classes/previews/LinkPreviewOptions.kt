package org.bezsahara.kittybot.telegram.classes.previews


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Describes the options used for link preview generation.
 * 
 * *[link](https://core.telegram.org/bots/api#linkpreviewoptions)*: https://core.telegram.org/bots/api#linkpreviewoptions
 * 
 * @param isDisabled Optional. True, if the link preview is disabled
 * @param url Optional. URL to use for the link preview. If empty, then the first URL found in the message text will be used
 * @param preferSmallMedia Optional. True, if the media in the link preview is supposed to be shrunk; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
 * @param preferLargeMedia Optional. True, if the media in the link preview is supposed to be enlarged; ignored if the URL isn't explicitly specified or media size change isn't supported for the preview
 * @param showAboveText Optional. True, if the link preview must be shown above the message text; otherwise, the link preview will be shown below the message text
 */
@Serializable
data class LinkPreviewOptions(
    @SerialName("is_disabled") val isDisabled: Boolean? = null,
    val url: String? = null,
    @SerialName("prefer_small_media") val preferSmallMedia: Boolean? = null,
    @SerialName("prefer_large_media") val preferLargeMedia: Boolean? = null,
    @SerialName("show_above_text") val showAboveText: Boolean? = null
)

