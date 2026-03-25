@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.telegram.utils.entity

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.keyboard.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.message.LinkPreviewOptions
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.message.ReplyParameters
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostParameters
import org.bezsahara.kittybot.telegram.utils.ParseMode
import org.bezsahara.kittybot.telegram.utils.forList

class EntityBuilder(stringSize: Int, entitySize: Int) : Appendable {
    constructor() : this(32,32)
    private val entityList = ArrayList<MessageEntity>(entitySize)

    private val stringBuilder = StringBuilder(stringSize)
    private var index: Long = 0

    override fun append(csq: CharSequence?): EntityBuilder {
        stringBuilder.append(csq)
        if (csq == null) {
            gatherStyles(4)
            index += 4
            return this
        }
        val s = csq.length.toLong()
        gatherStyles(s)
        index += s
        return this
    }

    override fun append(c: Char): EntityBuilder {
        addChar(c)
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): EntityBuilder {
        stringBuilder.append(csq, start, end)
        val l = (end - start).toLong()
        gatherStyles(l)
        index += l
        return this
    }

    fun addChar(char: Char) {
        stringBuilder.append(char)
        gatherStyles(1)
        index += 1
    }

    private fun gatherStyles(utf16Length: Long) {
        innerDeque?.let { d ->
            d.forList { finalType ->
                if (finalType is MessageEntityKind.Combined) {
                    finalType.mek.forEach {
                        entityList.add(it.toWire(index, utf16Length))
                    }
                } else {
                    entityList.add(finalType.toWire(index, utf16Length))
                }
            }
        }
    }

    fun addString(string: String) {
        stringBuilder.append(string)
        val utf16Length = string.length.toLong()
        gatherStyles(utf16Length)
        stringBuilder.length
        index += utf16Length
    }

    fun addString(
        string: String,
        type: MessageEntityKind
    ) {
        stringBuilder.append(string)
        val utf16Length = string.length.toLong()
        gatherStyles(utf16Length)
        if (type is MessageEntityKind.Combined) {
            type.mek.forEach {
                entityList.add(it.toWire(index, utf16Length))
            }
        } else {
            entityList.add(type.toWire(index, utf16Length))
        }
        index += utf16Length
    }

    private var innerDeque: ArrayDeque<MessageEntityKind>? = null
    val deque: ArrayDeque<MessageEntityKind> get() {
        var toResp = innerDeque
        if (toResp == null) {
            toResp = ArrayDeque<MessageEntityKind>(8)
            innerDeque = toResp
        }
        return toResp
    }

    inline fun styleOf(type: MessageEntityKind, block: () -> Unit) {
        deque.addLast(type)
        block()
        deque.removeLast()
    }


    /**
     * Usage, for example:
     * ```kotlin
     * Italic {
     *
     * }
     * ```
     */
    inline operator fun MessageEntityKind.invoke(block: () -> Unit) {
        deque.addLast(this)
        block()
        deque.removeLast()
    }

    fun getResult() = EntityString(entityList, stringBuilder.toString())
}

data class EntityString(
    val entities: List<MessageEntity>,
    val text: String
)

inline fun buildEntityString(block: EntityBuilder.() -> Unit): EntityString {
    val eb = EntityBuilder()
    eb.block()
    return eb.getResult()
}

suspend fun KittyBot.sendMessage(
    chatId: ChatId,
    entityString: EntityString,
    businessConnectionId: String? = null,
    messageThreadId: Long? = null,
    directMessagesTopicId: Long? = null,
    parseMode: ParseMode? = null,
    linkPreviewOptions: LinkPreviewOptions? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    allowPaidBroadcast: Boolean? = null,
    messageEffectId: String? = null,
    suggestedPostParameters: SuggestedPostParameters? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null
) = sendMessage(
    chatId,
    entityString.text,
    businessConnectionId,
    messageThreadId,
    directMessagesTopicId,
    parseMode,
    entityString.entities,
    linkPreviewOptions,
    disableNotification,
    protectContent,
    allowPaidBroadcast,
    messageEffectId,
    suggestedPostParameters,
    replyParameters,
    replyMarkup
)

internal fun example0() {
    buildEntityString {
        addString("Hi!", Italic + Bold + TextLink("google.com"))

        styleOf(Bold) {
            Italic {
                addString("Hi!")
            }
            (TextLink("facebook.com") + Underline) {
                addString("Come to us!!!")
            }
        }
    }
}