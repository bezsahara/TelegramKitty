package org.bezsahara.kittybot.telegram.utils

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScope
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllChatAdministrators
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllGroupChats
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeAllPrivateChats
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChat
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChatAdministrators
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeChatMember
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import kotlin.collections.component1
import kotlin.collections.component2


/**
 * Builds bot commands with [BotCommandsBuilder], sends every scoped command list via
 * [KittyBot.setMyCommands], and returns the grouped commands that were produced by the DSL.
 *
 * This is a thin DSL over Telegram's
 * [`setMyCommands`](https://core.telegram.org/bots/api#setmycommands) method. Each unique pair of
 * [BotCommandScope] and language code is collected into its own command list and submitted as a
 * separate API call.
 *
 * Commands created inside nested DSL blocks inherit the currently active scope and language. Scope
 * blocks cannot be nested inside other scope blocks, and language blocks cannot be nested inside
 * other language blocks.
 */
suspend inline fun KittyBot.setMyCommands(block: BotCommandsBuilder.() -> Unit): Map<SetCommandsKey, List<BotCommand>> {
    val builder = BotCommandsBuilder()
    builder.block()
    builder.execute(this).consume()
    return builder.get()
}

suspend fun KittyBot.setMyCommands(map: Map<SetCommandsKey, List<BotCommand>>): TResult<Boolean> {
    map.forEach { (key, cmds) ->
        val (scope, lang) = key

        setMyCommands(cmds, scope, lang, null).onError {
            return TResultFailure(it)
        }
    }

    return TResult(true)
}

/**
 * Builds a grouped command map with [BotCommandsBuilder] without sending it to Telegram.
 *
 * The returned map uses [SetCommandsKey] where:
 * - `scope == null` means Telegram's default command scope
 * - `lang == null` means the commands apply to users in that scope whose language has no dedicated
 * command list
 */
inline fun buildBotCommands(block: BotCommandsBuilder.() -> Unit): Map<SetCommandsKey, List<BotCommand>> {
    val builder = BotCommandsBuilder()
    builder.block()
    return builder.get()
}

/**
 * DSL builder for Telegram bot commands.
 *
 * Telegram stores commands per scope and optional language. This builder keeps the currently active
 * [BotCommandScope] and language code, groups commands by that pair, and later lets
 * [execute] submit every group through [KittyBot.setMyCommands].
 *
 * Supported scope blocks in this DSL map directly to Telegram command scopes:
 * - [allPrivateChats] for all private chats
 * - [allGroupChats] for all group and supergroup chats
 * - [allChatAdministrators] for administrators in all group and supergroup chats
 * - [chat] for a specific chat
 * - [chatAdministrators] for administrators of a specific group or supergroup
 * - [chatMembers] for a specific member of a specific group or supergroup
 *
 * `ChatId` follows Telegram semantics used by these scopes: it may contain a numeric chat id, and
 * scopes that support usernames accept a `@supergroupusername`.
 */
class BotCommandsBuilder {
    private var currentList: MutableList<BotCommand>? = null
    private var key = SetCommandsKey(null, null)
        set(value) {
            field = value
            currentList = null
        }

    private val map = linkedMapOf<SetCommandsKey, MutableList<BotCommand>>()

    fun addBotCommand(command: BotCommand) {
        var l = currentList
        if (l == null) {
            l = map.getOrPut(key) { mutableListOf() }
            currentList = l
        }
        l.add(command)
    }

    fun BotCommand.add() {
        addBotCommand(this)
    }

    /**
     * Creates and adds a [BotCommand] to the current scope and language group.
     *
     * Telegram requires:
     * - `command` to be 1..32 characters and contain only lowercase English letters, digits, and `_`
     * - `description` to be 1..256 characters
     */
    fun command(command: String, description: String) {
        addBotCommand(BotCommand(command, description))
    }
    
    /**
     * Runs [block] with a dedicated two-letter ISO 639-1 language code.
     *
     * Telegram applies commands with a language code only to users from the current scope whose
     * language matches that code. Only one language block can be active at a time.
     */
    inline fun language(lan: String, block: () -> Unit) {
        setCurrentLang(lan)
        try {
            block()
        } finally {
            removeLang()
        }
    }
    
    /**
     * Runs [block] in the `all_private_chats` scope.
     *
     * Commands defined here are visible in all private chats with the bot.
     */
    inline fun allPrivateChats(block: () -> Unit) {
        setCurrentScope(BotCommandScopeAllPrivateChats)
        try {
            block()
        } finally {
            removeScope()
        }
    }
    
    /**
     * Runs [block] in the `all_group_chats` scope.
     *
     * Commands defined here are visible in all group and supergroup chats.
     */
    inline fun allGroupChats(block: () -> Unit) {
        setCurrentScope(BotCommandScopeAllGroupChats)
        try {
            block()
        } finally {
            removeScope()
        }
    }
    
    /**
     * Runs [block] in the `all_chat_administrators` scope.
     *
     * Commands defined here are visible to administrators in all group and supergroup chats.
     */
    inline fun allChatAdministrators(block: () -> Unit) {
        setCurrentScope(BotCommandScopeAllChatAdministrators)
        try {
            block()
        } finally {
            removeScope()
        }
    }
    
    /**
     * Runs [block] in the `chat` scope for a specific chat.
     *
     * [chatId] may be a numeric chat id or a `@supergroupusername`. Channel direct messages chats
     * and channel chats are not supported.
     */
    inline fun chat(chatId: ChatId, block: () -> Unit) {
        setCurrentScope(BotCommandScopeChat(chatId))
        try {
            block()
        } finally {
            removeScope()
        }
    }
    
    /**
     * Runs [block] in the `chat_administrators` scope for a specific group or supergroup.
     *
     * [chatId] may be a numeric chat id or a `@supergroupusername`. Channel direct messages chats
     * and channel chats are not supported.
     */
    inline fun chatAdministrators(chatId: ChatId, block: () -> Unit) {
        setCurrentScope(BotCommandScopeChatAdministrators(chatId))
        try {
            block()
        } finally {
            removeScope()
        }
    }
    
    /**
     * Runs [block] in the `chat_member` scope for a specific member of a specific group or
     * supergroup.
     *
     * [chatId] may be a numeric chat id or a `@supergroupusername`. [userId] is the Telegram user
     * identifier of the target member. Channel direct messages chats and channel chats are not
     * supported.
     */
    inline fun chatMembers(chatId: ChatId, userId: Long, block: () -> Unit) {
        setCurrentScope(BotCommandScopeChatMember(chatId, userId))
        try {
            block()
        } finally {
            removeScope()
        }
    }

    fun ensureScopeIsNull() {
        if (key.scope != null) error("You cannot set scope in another scope block")
    }
    fun ensureLangIsNull() {
        if (key.lang != null) error("You cannot set language in another language scope")
    }

    fun removeLang() {
        key = SetCommandsKey(key.scope, null)
    }

    fun removeScope() {
        key = SetCommandsKey(null, key.lang)
    }

    fun setCurrentScope(scope: BotCommandScope) {
        ensureScopeIsNull()
        key = SetCommandsKey(scope, key.lang)
    }

    fun setCurrentLang(lan: String) {
        ensureLangIsNull()
        key = SetCommandsKey(key.scope, lan)
    }

    suspend fun execute(bot: KittyBot): TResult<Boolean> {
        map.forEach { (key, cmds) ->
            val (scope, lang) = key

            bot.setMyCommands(cmds, scope, lang, null).onError {
                return TResultFailure(it)
            }
        }

        return TResult(true)
    }

    fun get(): Map<SetCommandsKey, List<BotCommand>> {
        return map
    }
}

data class SetCommandsKey(val scope: BotCommandScope?, val lang: String?)

@PublishedApi
internal fun dslRequireNull(obj: Any?, msg: String) {
    if (obj != null) 
        error(msg)
}
