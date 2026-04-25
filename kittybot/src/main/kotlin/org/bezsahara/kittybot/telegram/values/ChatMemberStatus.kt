package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype ChatMember.status
 * - type ChatMemberOwner.status
 * - type ChatMemberAdministrator.status
 * - type ChatMemberMember.status
 * - type ChatMemberRestricted.status
 * - type ChatMemberLeft.status
 * - type ChatMemberBanned.status
 */
@Serializable(with = ChatMemberStatusSerializer::class)
class ChatMemberStatus internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<ChatMemberStatus.Known>() {
    companion object : ResolveEnumLikeBig<ChatMemberStatus, Known>(Known::class.java) {
        @JvmField
        val CREATOR = ChatMemberStatus("creator", Known.CREATOR).register()

        @JvmField
        val ADMINISTRATOR = ChatMemberStatus("administrator", Known.ADMINISTRATOR).register()

        @JvmField
        val MEMBER = ChatMemberStatus("member", Known.MEMBER).register()

        @JvmField
        val RESTRICTED = ChatMemberStatus("restricted", Known.RESTRICTED).register()

        @JvmField
        val LEFT = ChatMemberStatus("left", Known.LEFT).register()

        @JvmField
        val KICKED = ChatMemberStatus("kicked", Known.KICKED).register()

        override fun create(value: String): ChatMemberStatus {
            return ChatMemberStatus(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        CREATOR,
        ADMINISTRATOR,
        MEMBER,
        RESTRICTED,
        LEFT,
        KICKED;

        fun toChatMemberStatus(): ChatMemberStatus {
            return ChatMemberStatus.mapGet(this)
        }
    }

    override fun toString(): String {
        return "ChatMemberStatus($value)"
    }
}

internal object ChatMemberStatusSerializer : EnumLikeJsonSerializer<ChatMemberStatus>("ChatMemberStatus", ChatMemberStatus)

