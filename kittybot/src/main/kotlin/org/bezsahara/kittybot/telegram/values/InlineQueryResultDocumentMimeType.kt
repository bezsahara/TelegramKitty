package org.bezsahara.kittybot.telegram.values

import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import org.bezsahara.kittybot.bot.json.ResolveEnumLike


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - type InlineQueryResultDocument.mime_type
 */
@Serializable(with = InlineQueryResultDocumentMimeTypeSerializer::class)
class InlineQueryResultDocumentMimeType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<InlineQueryResultDocumentMimeType.Known>() {
    companion object : ResolveEnumLike<InlineQueryResultDocumentMimeType>() {
        @JvmField
        val APPLICATION_PDF = InlineQueryResultDocumentMimeType("application/pdf", Known.APPLICATION_PDF)

        @JvmField
        val APPLICATION_ZIP = InlineQueryResultDocumentMimeType("application/zip", Known.APPLICATION_ZIP)

        override fun resolve(value: String): InlineQueryResultDocumentMimeType {
            return when (value) {
                "application/pdf" -> APPLICATION_PDF
                "application/zip" -> APPLICATION_ZIP
                else -> InlineQueryResultDocumentMimeType(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        APPLICATION_PDF,
        APPLICATION_ZIP;

        fun toInlineQueryResultDocumentMimeType(): InlineQueryResultDocumentMimeType {
            return when (this) {
                APPLICATION_PDF -> InlineQueryResultDocumentMimeType.APPLICATION_PDF
                APPLICATION_ZIP -> InlineQueryResultDocumentMimeType.APPLICATION_ZIP
            }
        }
    }

    override fun toString(): String {
        return "InlineQueryResultDocumentMimeType($value)"
    }
}

internal object InlineQueryResultDocumentMimeTypeSerializer : EnumLikeJsonSerializer<InlineQueryResultDocumentMimeType>("InlineQueryResultDocumentMimeType", InlineQueryResultDocumentMimeType)

