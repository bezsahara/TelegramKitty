package org.bezsahara.kittybot.telegram.utils

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ChatAction {
    @SerialName("typing")
    TYPING,

    @SerialName("upload_photo")
    UPLOAD_PHOTO,

    @SerialName("record_video")
    RECORD_VIDEO,

    @SerialName("upload_video")
    UPLOAD_VIDEO,

    @SerialName("record_voice")
    RECORD_VOICE,

    @SerialName("upload_voice")
    UPLOAD_VOICE,

    @SerialName("upload_document")
    UPLOAD_DOCUMENT,

    @SerialName("choose_sticker")
    CHOOSE_STICKER,

    @SerialName("find_location")
    FIND_LOCATION,

    @SerialName("record_video_note")
    RECORD_VIDEO_NOTE,

    @SerialName("upload_video_note")
    UPLOAD_VIDEO_NOTE
}