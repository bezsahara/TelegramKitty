package org.bezsahara.kittybot.telegram.classes.messages.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EntityType {
    @SerialName("mention")
    MENTION,

    @SerialName("hashtag")
    HASHTAG,

    @SerialName("cashtag")
    CASHTAG,

    @SerialName("bot_command")
    BOT_COMMAND,

    @SerialName("url")
    URL,

    @SerialName("email")
    EMAIL,

    @SerialName("phone_number")
    PHONE_NUMBER,

    @SerialName("bold")
    BOLD,

    @SerialName("italic")
    ITALIC,

    @SerialName("underline")
    UNDERLINE,

    @SerialName("strikethrough")
    STRIKETHROUGH,

    @SerialName("spoiler")
    SPOILER,

    @SerialName("blockquote")
    BLOCKQUOTE,

    @SerialName("code")
    CODE,

    @SerialName("pre")
    PRE,

    @SerialName("text_link")
    TEXT_LINK,

    @SerialName("text_mention")
    TEXT_MENTION,

    @SerialName("custom_emoji")
    CUSTOM_EMOJI
}