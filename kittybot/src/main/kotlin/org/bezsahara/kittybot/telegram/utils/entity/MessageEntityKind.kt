package org.bezsahara.kittybot.telegram.utils.entity

import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.values.MsgEntityType
import java.util.*

object Mention : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.MENTION, offset, length)
}

object Hashtag : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.HASHTAG, offset, length)
}

object Cashtag : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.CASHTAG, offset, length)
}

object BotCommand : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.BOT_COMMAND, offset, length)
}

object Url : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.URL, offset, length)
}

object Email : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.EMAIL, offset, length)
}

object PhoneNumber : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.PHONE_NUMBER, offset, length)
}

object Bold : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.BOLD, offset, length)
}

object Italic : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.ITALIC, offset, length)
}

object Underline : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.UNDERLINE, offset, length)
}

object Strikethrough : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.STRIKETHROUGH, offset, length)
}

object Spoiler : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.SPOILER, offset, length)
}

object Blockquote : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.BLOCKQUOTE, offset, length)
}

object Code : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.CODE, offset, length)
}

// kinds with payload
class Pre(val language: String? = null) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.PRE, offset, length, language = language)
}

class TextLink(val url: String) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.TEXT_LINK, offset, length, url = url)
}

class TextMention(val user: User) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.TEXT_MENTION, offset, length, user = user)
}

class CustomEmoji(val customEmojiId: String) : MessageEntityKind() {
    override fun toWire(offset: Long, length: Long) =
        MessageEntity(MsgEntityType.CUSTOM_EMOJI, offset, length, customEmojiId = customEmojiId)
}

class DateTime(val unixTime: Long, val dateTimeFormat: String? = null) : MessageEntityKind() {
    override fun toWire(
        offset: Long,
        length: Long,
    ): MessageEntity {
        return MessageEntity(MsgEntityType.DATE_TIME, offset, length, dateTimeFormat = dateTimeFormat, unixTime = unixTime)
    }
}

sealed class MessageEntityKind {

    abstract fun toWire(offset: Long, length: Long): MessageEntity

    open operator fun plus(other: MessageEntityKind): MessageEntityKind {
        val arr = if (other is Combined) {
            val mek = other.mek
            Arrays.copyOf(mek, mek.size + 1, Array<MessageEntityKind>::class.java).also { it[mek.size] = this }
        } else {
            arrayOf(this, other)
        }

        return Combined(arr)
    }

    class Combined(val mek: Array<MessageEntityKind>) : MessageEntityKind() {
        override fun toWire(
            offset: Long,
            length: Long,
        ): MessageEntity { error("Not supported") }

        override fun plus(other: MessageEntityKind): MessageEntityKind {
            val arr = if (other is Combined) {
                arrayOfNulls<MessageEntityKind>(mek.size + other.mek.size).also {
                    System.arraycopy(mek, 0, it, 0, mek.size)
                    System.arraycopy(other.mek, 0, it, mek.size, other.mek.size)
                } as Array<MessageEntityKind>
            } else {
                Arrays.copyOf(mek, mek.size + 1, Array<MessageEntityKind>::class.java).also { it[mek.size] = other }
            }

            return Combined(arr)
        }
    }

    companion object {
        fun of(vararg mek: MessageEntityKind): MessageEntityKind {
            if (mek.size == 1) {
                return mek[0]
            }
            return Combined(mek as Array<MessageEntityKind>)
        }
    }
}
