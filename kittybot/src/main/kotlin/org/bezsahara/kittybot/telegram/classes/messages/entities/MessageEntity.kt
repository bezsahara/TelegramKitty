package org.bezsahara.kittybot.telegram.classes.messages.entities


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.telegram.classes.users.User


/**
 * This object represents one special entity in a text message. For example, hashtags, usernames, URLs, etc.
 * 
 * *[link](https://core.telegram.org/bots/api#messageentity)*: https://core.telegram.org/bots/api#messageentity
 * 
 * @param type Type of the entity. Currently, can be "mention" (@username), "hashtag" (#hashtag), "cashtag" ($USD), "bot_command" (/start@jobs_bot), "url" (https://telegram.org), "email" (do-not-reply@telegram.org), "phone_number" (+1-212-555-0123), "bold" (bold text), "italic" (italic text), "underline" (underlined text), "strikethrough" (strikethrough text), "spoiler" (spoiler message), "blockquote" (block quotation), "code" (monowidth string), "pre" (monowidth block), "text_link" (for clickable text URLs), "text_mention" (for users without usernames), "custom_emoji" (for inline custom emoji stickers)
 * @param offset Offset in UTF-16 code units to the start of the entity
 * @param length Length of the entity in UTF-16 code units
 * @param url Optional. For "text_link" only, URL that will be opened after user taps on the text
 * @param user Optional. For "text_mention" only, the mentioned user
 * @param language Optional. For "pre" only, the programming language of the entity text
 * @param customEmojiId Optional. For "custom_emoji" only, unique identifier of the custom emoji. Use getCustomEmojiStickers to get full information about the sticker
 */
@Serializable
data class MessageEntity(
    val type: EntityType,
    val offset: Long,
    val length: Long,
    val url: String? = null,
    val user: User? = null,
    val language: String? = null,
    @SerialName("custom_emoji_id") val customEmojiId: String? = null
)

