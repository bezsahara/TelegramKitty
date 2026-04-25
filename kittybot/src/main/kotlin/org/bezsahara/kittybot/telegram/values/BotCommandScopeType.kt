package org.bezsahara.kittybot.telegram.values

import org.bezsahara.kittybot.bot.json.EnumLike
import org.bezsahara.kittybot.bot.json.EnumLikeJsonSerializer
import kotlinx.serialization.Serializable
import org.bezsahara.kittybot.bot.json.ResolveEnumLikeBig


/**
 * String-backed constrained Telegram value.
 *
 * Used by:
 * - supertype BotCommandScope.type
 * - type BotCommandScopeDefault.type
 * - type BotCommandScopeAllPrivateChats.type
 * - type BotCommandScopeAllGroupChats.type
 * - type BotCommandScopeAllChatAdministrators.type
 * - type BotCommandScopeChat.type
 * - type BotCommandScopeChatAdministrators.type
 * - type BotCommandScopeChatMember.type
 */
@Serializable(with = BotCommandScopeTypeSerializer::class)
class BotCommandScopeType internal constructor(
    override val value: String,
    override val known: Known?,
) : EnumLike<BotCommandScopeType.Known>() {
    companion object : ResolveEnumLikeBig<BotCommandScopeType, Known>(Known::class.java) {
        @JvmField
        val DEFAULT = BotCommandScopeType("default", Known.DEFAULT).register()

        @JvmField
        val ALL_PRIVATE_CHATS = BotCommandScopeType("all_private_chats", Known.ALL_PRIVATE_CHATS).register()

        @JvmField
        val ALL_GROUP_CHATS = BotCommandScopeType("all_group_chats", Known.ALL_GROUP_CHATS).register()

        @JvmField
        val ALL_CHAT_ADMINISTRATORS = BotCommandScopeType("all_chat_administrators", Known.ALL_CHAT_ADMINISTRATORS).register()

        @JvmField
        val CHAT = BotCommandScopeType("chat", Known.CHAT).register()

        @JvmField
        val CHAT_ADMINISTRATORS = BotCommandScopeType("chat_administrators", Known.CHAT_ADMINISTRATORS).register()

        @JvmField
        val CHAT_MEMBER = BotCommandScopeType("chat_member", Known.CHAT_MEMBER).register()

        override fun create(value: String): BotCommandScopeType {
            return BotCommandScopeType(value, null)
        }
    }

    fun requireKnown(): Known {
        return known ?: error("Value $value is not known. Try updating library to latest version")
    }

    enum class Known {
        DEFAULT,
        ALL_PRIVATE_CHATS,
        ALL_GROUP_CHATS,
        ALL_CHAT_ADMINISTRATORS,
        CHAT,
        CHAT_ADMINISTRATORS,
        CHAT_MEMBER;

        fun toBotCommandScopeType(): BotCommandScopeType {
            return BotCommandScopeType.mapGet(this)
        }
    }

    override fun toString(): String {
        return "BotCommandScopeType($value)"
    }
}

internal object BotCommandScopeTypeSerializer : EnumLikeJsonSerializer<BotCommandScopeType>("BotCommandScopeType", BotCommandScopeType)

