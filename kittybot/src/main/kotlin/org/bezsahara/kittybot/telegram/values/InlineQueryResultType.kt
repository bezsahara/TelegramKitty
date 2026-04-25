package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype InlineQueryResult.type
 * - type InlineQueryResultCachedAudio.type
 * - type InlineQueryResultCachedDocument.type
 * - type InlineQueryResultCachedGif.type
 * - type InlineQueryResultCachedMpeg4Gif.type
 * - type InlineQueryResultCachedPhoto.type
 * - type InlineQueryResultCachedSticker.type
 * - type InlineQueryResultCachedVideo.type
 * - type InlineQueryResultCachedVoice.type
 * - type InlineQueryResultArticle.type
 * - type InlineQueryResultAudio.type
 * - type InlineQueryResultContact.type
 * - type InlineQueryResultGame.type
 * - type InlineQueryResultDocument.type
 * - type InlineQueryResultGif.type
 * - type InlineQueryResultLocation.type
 * - type InlineQueryResultMpeg4Gif.type
 * - type InlineQueryResultPhoto.type
 * - type InlineQueryResultVenue.type
 * - type InlineQueryResultVideo.type
 * - type InlineQueryResultVoice.type
 */
@Serializable(with = InlineQueryResultTypeSerializer::class)
class InlineQueryResultType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InlineQueryResultType.Known>() {
    companion object : ResolveEnumLikeBig<InlineQueryResultType, Known>(Known::class.java) {
        @JvmField
        val AUDIO = InlineQueryResultType("audio", Known.AUDIO).register()

        @JvmField
        val DOCUMENT = InlineQueryResultType("document", Known.DOCUMENT).register()

        @JvmField
        val GIF = InlineQueryResultType("gif", Known.GIF).register()

        @JvmField
        val MPEG4_GIF = InlineQueryResultType("mpeg4_gif", Known.MPEG4_GIF).register()

        @JvmField
        val PHOTO = InlineQueryResultType("photo", Known.PHOTO).register()

        @JvmField
        val STICKER = InlineQueryResultType("sticker", Known.STICKER).register()

        @JvmField
        val VIDEO = InlineQueryResultType("video", Known.VIDEO).register()

        @JvmField
        val VOICE = InlineQueryResultType("voice", Known.VOICE).register()

        @JvmField
        val ARTICLE = InlineQueryResultType("article", Known.ARTICLE).register()

        @JvmField
        val CONTACT = InlineQueryResultType("contact", Known.CONTACT).register()

        @JvmField
        val GAME = InlineQueryResultType("game", Known.GAME).register()

        @JvmField
        val LOCATION = InlineQueryResultType("location", Known.LOCATION).register()

        @JvmField
        val VENUE = InlineQueryResultType("venue", Known.VENUE).register()

        override fun create(value: String): InlineQueryResultType {
            return InlineQueryResultType(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        AUDIO,
        DOCUMENT,
        GIF,
        MPEG4_GIF,
        PHOTO,
        STICKER,
        VIDEO,
        VOICE,
        ARTICLE,
        CONTACT,
        GAME,
        LOCATION,
        VENUE;

        fun toInlineQueryResultType(): InlineQueryResultType {
            return InlineQueryResultType.mapGet(this)
        }
    }

    override fun toString(): String {
        return "InlineQueryResultType($value)"
    }
}

internal object InlineQueryResultTypeSerializer : EnumLikeJsonSerializer<InlineQueryResultType>("InlineQueryResultType", InlineQueryResultType)

