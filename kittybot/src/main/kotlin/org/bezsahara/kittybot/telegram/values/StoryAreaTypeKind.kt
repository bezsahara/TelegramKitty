package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.ResolveEnumLike
import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype StoryAreaType.type
 * - type StoryAreaTypeLocation.type
 * - type StoryAreaTypeSuggestedReaction.type
 * - type StoryAreaTypeLink.type
 * - type StoryAreaTypeWeather.type
 * - type StoryAreaTypeUniqueGift.type
 */
@Serializable(with = StoryAreaTypeKindSerializer::class)
class StoryAreaTypeKind internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<StoryAreaTypeKind.Known>() {
    companion object : ResolveEnumLike<StoryAreaTypeKind>() {
        @JvmField
        val LOCATION = StoryAreaTypeKind("location", Known.LOCATION)

        @JvmField
        val SUGGESTED_REACTION = StoryAreaTypeKind("suggested_reaction", Known.SUGGESTED_REACTION)

        @JvmField
        val LINK = StoryAreaTypeKind("link", Known.LINK)

        @JvmField
        val WEATHER = StoryAreaTypeKind("weather", Known.WEATHER)

        @JvmField
        val UNIQUE_GIFT = StoryAreaTypeKind("unique_gift", Known.UNIQUE_GIFT)

        override fun resolve(value: String): StoryAreaTypeKind {
            return when (value) {
                "location" -> LOCATION
                "suggested_reaction" -> SUGGESTED_REACTION
                "link" -> LINK
                "weather" -> WEATHER
                "unique_gift" -> UNIQUE_GIFT
                else -> StoryAreaTypeKind(value, null)
            }
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        LOCATION,
        SUGGESTED_REACTION,
        LINK,
        WEATHER,
        UNIQUE_GIFT;

        fun toStoryAreaTypeKind(): StoryAreaTypeKind {
            return when (this) {
                LOCATION -> StoryAreaTypeKind.LOCATION
                SUGGESTED_REACTION -> StoryAreaTypeKind.SUGGESTED_REACTION
                LINK -> StoryAreaTypeKind.LINK
                WEATHER -> StoryAreaTypeKind.WEATHER
                UNIQUE_GIFT -> StoryAreaTypeKind.UNIQUE_GIFT
            }
        }
    }

    override fun toString(): String {
        return "StoryAreaTypeKind($value)"
    }
}

internal object StoryAreaTypeKindSerializer : EnumLikeJsonSerializer<StoryAreaTypeKind>("StoryAreaTypeKind", StoryAreaTypeKind)

