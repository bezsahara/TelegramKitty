@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.telegram.utils.entity

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.other.KotlinHelpers
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.keyboard.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.message.LinkPreviewOptions
import org.bezsahara.kittybot.telegram.classes.message.MessageEntity
import org.bezsahara.kittybot.telegram.classes.message.ReplyParameters
import org.bezsahara.kittybot.telegram.classes.message.SuggestedPostParameters
import org.bezsahara.kittybot.telegram.values.ParseMode

class EntityBuilder(stringSize: Int, entitySize: Int) : Appendable {
    constructor() : this(32, 8)
    private val entityList = ArrayList<MessageEntity>(entitySize)

    private val stringBuilder = StringBuilder(stringSize)
    private var index: Long = 0

    override fun append(csq: CharSequence?): EntityBuilder {
        stringBuilder.append(csq)
        if (csq == null) {
            index += 4
            return this
        }
        val s = csq.length.toLong()
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
        index += l
        return this
    }

    fun append(char: Char, type: EntityKind): EntityBuilder {
        addChar(char, type)
        return this
    }

    fun append(
        string: String?,
        type: EntityKind
    ): EntityBuilder {
        addString(string, type)
        return this
    }

    fun append(
        string: String?,
        vararg type: EntityKind
    ): EntityBuilder {
        return KotlinHelpers.append(this, string, type)
    }

    fun addChar(char: Char) {
        stringBuilder.append(char)
        index += 1
    }

    fun addChar(char: Char, type: EntityKind) {
        stringBuilder.append(char)
        addStyle(type, 1)
        index += 1
    }

    fun addString(string: String?) {
        stringBuilder.append(string)
        val utf16Length = string?.length?.toLong() ?: 4
        index += utf16Length
    }

    fun addString(
        string: String?,
        type: EntityKind
    ) {
        stringBuilder.append(string)
        val utf16Length = string?.length?.toLong() ?: 4
        addStyle(type, utf16Length)
        index += utf16Length
    }

    fun addString(
        string: String?,
        vararg types: EntityKind
    ) {
        stringBuilder.append(string)
        val utf16Length = string?.length?.toLong() ?: 4
        types.forEach { type ->
            addStyle(type, utf16Length)
        }
        index += utf16Length
    }

    private fun addStyle(type: EntityKind, utf16Length: Long) {
        if (type is EntityKind.Combined) {
            type.mek.forEach {
                entityList.add(it.toWire(index, utf16Length))
            }
        } else {
            entityList.add(type.toWire(index, utf16Length))
        }
    }


    @PublishedApi
    internal fun index(): Long {
        return index
    }

    @PublishedApi
    internal fun removeScope(type: EntityKind, lastIndex: Long) {
        val i = index
        if (i == lastIndex) return
        addStyle(type, i - lastIndex)
    }

    inline fun styleOf(type: EntityKind, block: () -> Unit) {
        val lastIndex = index()
        try {
            block()
        } finally {
            removeScope(type, lastIndex)
        }
    }


    /**
     * Usage, for example:
     * ```kotlin
     * Italic {
     *
     * }
     * ```
     */
    inline operator fun EntityKind.invoke(block: () -> Unit) {
        styleOf(this, block)
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
        appendLine()
        addString("Hi!", EntityKind.of(Italic))
        appendLine()
        addString("Hi!", TextLink("google.com"), Italic, Bold)

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
