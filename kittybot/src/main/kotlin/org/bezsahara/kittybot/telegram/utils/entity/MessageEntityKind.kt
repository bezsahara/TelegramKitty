package org.bezsahara.kittybot.telegram.utils.entity

import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.user.User
import org.bezsahara.kittybot.telegram.values.MsgEntityType
import java.util.*

typealias Mention = EntityKind.Mention

typealias Hashtag = EntityKind.Hashtag

typealias Cashtag = EntityKind.Cashtag

typealias BotCommand = EntityKind.BotCommand

typealias Url = EntityKind.Url

typealias Email = EntityKind.Email

typealias PhoneNumber = EntityKind.PhoneNumber

typealias Bold = EntityKind.Bold

typealias Italic = EntityKind.Italic

typealias Underline = EntityKind.Underline

typealias Strikethrough = EntityKind.Strikethrough

typealias Spoiler = EntityKind.Spoiler

typealias Blockquote = EntityKind.Blockquote

typealias Code = EntityKind.Code

// kinds with payload
typealias Pre = EntityKind.Pre

typealias TextLink = EntityKind.TextLink

typealias TextMention = EntityKind.TextMention

typealias CustomEmoji = EntityKind.CustomEmoji

typealias DateTime = EntityKind.DateTime

typealias MessageEntityKind = EntityKind

sealed class EntityKind {

    object Mention : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.MENTION, offset, length)
    }

    object Hashtag : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.HASHTAG, offset, length)
    }

    object Cashtag : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.CASHTAG, offset, length)
    }

    object BotCommand : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.BOT_COMMAND, offset, length)
    }

    object Url : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.URL, offset, length)
    }

    object Email : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.EMAIL, offset, length)
    }

    object PhoneNumber : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.PHONE_NUMBER, offset, length)
    }

    object Bold : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.BOLD, offset, length)
    }

    object Italic : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.ITALIC, offset, length)
    }

    object Underline : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.UNDERLINE, offset, length)
    }

    object Strikethrough : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.STRIKETHROUGH, offset, length)
    }

    object Spoiler : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.SPOILER, offset, length)
    }

    object Blockquote : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.BLOCKQUOTE, offset, length)
    }

    object Code : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.CODE, offset, length)
    }

    // kinds with payload
    class Pre(val language: String? = null) : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.PRE, offset, length, language = language)
    }

    class TextLink(val url: String) : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.TEXT_LINK, offset, length, url = url)
    }

    class TextMention(val user: User) : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.TEXT_MENTION, offset, length, user = user)
    }

    class CustomEmoji(val customEmojiId: String) : EntityKind() {
        override fun toWire(offset: Long, length: Long) =
            MessageEntity(MsgEntityType.CUSTOM_EMOJI, offset, length, customEmojiId = customEmojiId)
    }

    class DateTime(val unixTime: Long, val dateTimeFormat: String? = null) : EntityKind() {
        override fun toWire(
            offset: Long,
            length: Long,
        ): MessageEntity {
            return MessageEntity(MsgEntityType.DATE_TIME, offset, length, dateTimeFormat = dateTimeFormat, unixTime = unixTime)
        }
    }

    class CustomClass(val entityKind: MessageEntity) : EntityKind() {
        override fun toWire(
            offset: Long,
            length: Long,
        ): MessageEntity {
            return entityKind.copy(offset = offset, length = length)
        }
    }

    abstract fun toWire(offset: Long, length: Long): MessageEntity

    open operator fun plus(other: EntityKind): EntityKind {
        val arr = if (other is Combined) {
            val mek = other.mek
            Arrays.copyOf(mek, mek.size + 1, Array<EntityKind>::class.java).also { it[mek.size] = this }
        } else {
            arrayOf(this, other)
        }

        return Combined(arr)
    }

    class Combined(val mek: Array<EntityKind>) : EntityKind() {
        override fun toWire(
            offset: Long,
            length: Long,
        ): MessageEntity { error("Not supported") }

        override fun plus(other: EntityKind): EntityKind {
            val arr = if (other is Combined) {
                arrayOfNulls<EntityKind>(mek.size + other.mek.size).also {
                    System.arraycopy(mek, 0, it, 0, mek.size)
                    System.arraycopy(other.mek, 0, it, mek.size, other.mek.size)
                } as Array<EntityKind>
            } else {
                Arrays.copyOf(mek, mek.size + 1, Array<EntityKind>::class.java).also { it[mek.size] = other }
            }

            return Combined(arr)
        }
    }

    companion object {
        fun of(vararg mek: EntityKind): EntityKind {
            if (mek.size == 1) {
                return mek[0]
            }
            return Combined(mek as Array<EntityKind>)
        }
    }
}
