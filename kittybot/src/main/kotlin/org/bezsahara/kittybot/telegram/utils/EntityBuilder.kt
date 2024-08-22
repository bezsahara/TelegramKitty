package org.bezsahara.kittybot.telegram.utils

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.errors.hiss
import org.bezsahara.kittybot.telegram.classes.chats.ChatId
import org.bezsahara.kittybot.telegram.classes.keyboards.ReplyMarkup
import org.bezsahara.kittybot.telegram.classes.messages.entities.EntityType
import org.bezsahara.kittybot.telegram.classes.messages.entities.MessageEntity
import org.bezsahara.kittybot.telegram.classes.previews.LinkPreviewOptions
import org.bezsahara.kittybot.telegram.classes.replies.ReplyParameters


val String.utf16Length
    get() = Character.codePointCount(this, 0, this.length).toLong()

class EntityBuilder {
    private val entityList = mutableListOf<MessageEntity>()

    val newLine: Unit
        get() {
            addString("\n")
        }

    private val stringBuilder = StringBuilder()
    private var index: Long = 0

    fun addString(string: String) {
        stringBuilder.append(string)
        index += string.utf16Length
    }

    fun addString(
        string: String,
        type: EntityType,
        url: String? = null,
        user: org.bezsahara.kittybot.telegram.classes.users.User? = null,
        language: String? = null,
        customEmojiId: String? = null
    ) {
        when (type) {
            EntityType.PRE -> if (language == null) hiss("With EntityType.PRE you need to provide 'language' argument")
            EntityType.TEXT_LINK -> if (url == null) hiss("With EntityType.TEXT_LINK you need to provide 'url' argument")
            EntityType.TEXT_MENTION -> if (user == null) hiss("With EntityType.TEXT_MENTION you need to provide 'user' argument")
            EntityType.CUSTOM_EMOJI -> if (customEmojiId == null) hiss("With EntityType.CUSTOM_EMOJI you need to provide 'customEmojiId' argument")
            else -> Unit
        }
        stringBuilder.append(string)
        entityList.add(
            MessageEntity(
                type = type,
                index,
                string.utf16Length,
                url,
                user,
                language,
                customEmojiId
            )
        )
        index += string.utf16Length
    }

    fun addTextLink(string: String, url: String) = addString(string, EntityType.TEXT_LINK, url)

    fun addEntities(string: String, entities: List<MessageEntity>?) {
        if (entities != null) {
            val baseOffset = index
            entityList.addAll(
                entities.map {
                    val newEntity = MessageEntity(
                        it.type,
                        it.offset + baseOffset,
                        it.length,
                        it.url,
                        it.user,
                        it.language
                    )
                    index += newEntity.length
                    newEntity
                }
            )
        } else {
            index += string.utf16Length
        }
        stringBuilder.append(string)
    }

    data class EntityString(
        val entities: List<MessageEntity>,
        val text: String
    )

    fun getResult() = EntityString(entityList, stringBuilder.toString())
}

suspend fun KittyBot.sendMessage(
    chatId: ChatId,
    entityString: EntityBuilder.EntityString,
    businessConnectionId: String? = null,
    messageThreadId: Long? = null,
    linkPreviewOptions: LinkPreviewOptions? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null
) = sendMessage(
    chatId,
    entityString.text,
    businessConnectionId,
    messageThreadId,
    null,
    entityString.entities,
    linkPreviewOptions,
    disableNotification,
    protectContent,
    replyParameters,
    replyMarkup
)

inline fun buildEntityString(builder: EntityBuilder.() -> Unit): EntityBuilder.EntityString {
    return EntityBuilder().apply(builder).getResult()
}