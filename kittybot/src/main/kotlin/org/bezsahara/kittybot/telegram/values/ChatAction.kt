package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - method sendChatAction.action
 */
@Serializable(with = ChatActionSerializer::class)
class ChatAction internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ChatAction.Known>() {
    companion object : ResolveEnumLikeBig<ChatAction, Known>(Known::class.java) {
        @JvmField
        val TYPING = ChatAction("typing", Known.TYPING).register()

        @JvmField
        val UPLOAD_PHOTO = ChatAction("upload_photo", Known.UPLOAD_PHOTO).register()

        @JvmField
        val RECORD_VIDEO = ChatAction("record_video", Known.RECORD_VIDEO).register()

        @JvmField
        val UPLOAD_VIDEO = ChatAction("upload_video", Known.UPLOAD_VIDEO).register()

        @JvmField
        val RECORD_VOICE = ChatAction("record_voice", Known.RECORD_VOICE).register()

        @JvmField
        val UPLOAD_VOICE = ChatAction("upload_voice", Known.UPLOAD_VOICE).register()

        @JvmField
        val UPLOAD_DOCUMENT = ChatAction("upload_document", Known.UPLOAD_DOCUMENT).register()

        @JvmField
        val CHOOSE_STICKER = ChatAction("choose_sticker", Known.CHOOSE_STICKER).register()

        @JvmField
        val FIND_LOCATION = ChatAction("find_location", Known.FIND_LOCATION).register()

        @JvmField
        val RECORD_VIDEO_NOTE = ChatAction("record_video_note", Known.RECORD_VIDEO_NOTE).register()

        @JvmField
        val UPLOAD_VIDEO_NOTE = ChatAction("upload_video_note", Known.UPLOAD_VIDEO_NOTE).register()

        override fun create(value: String): ChatAction {
            return ChatAction(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        TYPING,
        UPLOAD_PHOTO,
        RECORD_VIDEO,
        UPLOAD_VIDEO,
        RECORD_VOICE,
        UPLOAD_VOICE,
        UPLOAD_DOCUMENT,
        CHOOSE_STICKER,
        FIND_LOCATION,
        RECORD_VIDEO_NOTE,
        UPLOAD_VIDEO_NOTE;

        fun toChatAction(): ChatAction {
            return ChatAction.mapGet(this)
        }
    }

    override fun toString(): String {
        return "ChatAction($value)"
    }
}

internal object ChatActionSerializer : EnumLikeJsonSerializer<ChatAction>("ChatAction", ChatAction)

