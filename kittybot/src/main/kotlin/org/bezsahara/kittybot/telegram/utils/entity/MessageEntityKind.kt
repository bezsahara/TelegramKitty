package org.bezsahara.kittybot.telegram.utils.entity

import org.bezsahara.kittybot.telegram.classes.message.EntityType
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.user.User
import java.util.*

object Mention : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.MENTION, offset, length)
}

object Hashtag : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.HASHTAG, offset, length)
}

object Cashtag : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.CASHTAG, offset, length)
}

object BotCommand : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.BOT_COMMAND, offset, length)
}

object Url : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.URL, offset, length)
}

object Email : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.EMAIL, offset, length)
}

object PhoneNumber : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.PHONE_NUMBER, offset, length)
}

object Bold : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.BOLD, offset, length)
}

object Italic : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.ITALIC, offset, length)
}

object Underline : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.UNDERLINE, offset, length)
}

object Strikethrough : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.STRIKETHROUGH, offset, length)
}

object Spoiler : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.SPOILER, offset, length)
}

object Blockquote : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.BLOCKQUOTE, offset, length)
}

object Code : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.CODE, offset, length)
}

// kinds with payload
class Pre(val language: String? = null) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.PRE, offset, length, language = language)
}

class TextLink(val url: String) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.TEXT_LINK, offset, length, url = url)
}

class TextMention(val user: User) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.TEXT_MENTION, offset, length, user = user)
}

class CustomEmoji(val customEmojiId: String) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(EntityType.CUSTOM_EMOJI, offset, length, customEmojiId = customEmojiId)
}

sealed class MessageEntityKind {

    abstract fun toWire(offset: Long, length: Long): MessageEntity

    open operator fun plus(other: MessageEntityKind): MessageEntityKind {
        val arr = if (other is Combined) {
            val mek = other.mek
            arrayOfNulls<MessageEntityKind>(mek.size + 1).also {
                repeat(other.mek.size) { i ->
                    it[i + 1] = other.mek[i]
                }
            }
        } else arrayOfNulls<MessageEntityKind>(2).also {
            it[0] = this
            it[1] = other
        }

        return Combined(arr as Array<MessageEntityKind>)
    }

    class Combined(val mek: Array<MessageEntityKind>) : MessageEntityKind() {
        override fun toWire(
            offset: Long,
            length: Long,
        ): MessageEntity { error("Not supported") }

        override fun plus(other: MessageEntityKind): MessageEntityKind {
            val arr = if (other is Combined) {
                mek + other.mek
            } else {
                Arrays.copyOf(mek, mek.size + 1).also { it[mek.size] = other }
            }

            return Combined(arr)
        }
    }
}
