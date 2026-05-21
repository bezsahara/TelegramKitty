# TelegramKitty Usage Guide For Agents

TelegramKitty is a Kotlin-first Telegram Bot API wrapper. The generated Telegram
models and API client live in `kittybot`; the default Vert.x HTTP transport and
some helper extensions live in `kittybot-client`.

Use this file when writing bots with the library or when modifying examples. Prefer
the dispatcher DSL where possible. Drop to raw handlers only when the DSL cannot
express the behavior you need.

## Configuration

### Dependencies

Use both artifacts for the normal setup with the default Vert.x client:

```kotlin
dependencies {
    implementation("org.bezsahara:kittybot:4.0.0")
    implementation("org.bezsahara:kittybot-client:4.0.0")
}
```

Use only `kittybot` if you provide your own HTTP client through `useCustomClient`
or `apiClientBuilder`:

```kotlin
dependencies {
    implementation("org.bezsahara:kittybot:4.0.0")
}
```

The project targets JVM 17 and uses Kotlin coroutines. Telegram API methods are
generated as `suspend` functions returning `TResult<T>`. If the bot is wrapped
with `throwErrorsOnFailure()`, Telegram failures throw before the `TResult` is
returned.

### Minimal Polling Bot

`KittyBot<PollingReceiver> { ... }` returns `KittyBotConfig<PollingReceiver>`,
not the raw API client. The generated API client is available as
`config.kittyBot`, and inside handler scopes as `bot`.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver

fun main() {
    val bot = KittyBot<PollingReceiver> {
        token = System.getenv("BOT_TOKEN")
        ensureOnlyNewUpdates()

        dispatchers {
            command("/start") {
                bot.sendMessage(chatId, "Hi from TelegramKitty").consume()
            }
        }
    }

    bot.startPolling()
}
```

If you do not want `startPolling()` to block the current thread, call
`startPolling(wait = false)` and keep the returned `Job`.

### Polling Configuration

Polling is configured inside `KittyBot<PollingReceiver> { ... }`.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.builder.pollingTimeout
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.updates.updaters.MultiIdentity
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import java.net.URI

val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")

    // Optional. Use this for a local Bot API server or proxy.
    baseUri = URI.create("https://api.telegram.org")

    // Long polling timeout in seconds.
    pollingTimeout = 60

    // Ask Telegram only for the update kinds this bot handles.
    allowUpdatesOf(MessageUpdate, CallbackQueryUpdate)

    // Prevent replaying old updates from before this process started.
    ensureOnlyNewUpdates()

    // Ordered parallelism by chat/user identity.
    updaterMode = UpdaterMode.MultiThread(
        MultiIdentity.OfAnyUserChatIdentity,
        parallelism = 16
    )

    // Central handler error policy. Return value controls dispatch after the error.
    setErrorHandler { throwable, bot, update, _, _ ->
        update.chatIdOrNull()?.let { chatId ->
            bot.sendMessage(chatId, "Handler failed: ${throwable.message}")
        }
        Decision.Consumed
    }

    dispatchers {
        command("/ping") {
            bot.sendMessage(chatId, "pong")
        }
    }
}
```

Polling notes:

- `token` is required. Usually read it from `System.getenv("BOT_TOKEN")`.
- `pollingTimeout` is only available for `PollingReceiver`.
- `allowUpdatesOf(...)` is passed to polling `getUpdates` as Telegram
  `allowed_updates`.
- `ensureOnlyNewUpdates()` calls Telegram during initialization and discards
  pending updates. It is useful after restarts.
- `ensureOnlyNewUpdatesWithFile(file)` persists the last handled update id on
  close and recovers it on startup.
- `ensureOnlyNewUpdatesCustom(engine)` and `ensureOnlyNewUpdates(onSave, onRecover)`
  let you provide your own last-update-id storage.
- If a webhook was set before, delete it before polling:
  `init { deleteWebhook() }`.

### Webhook Configuration

Webhook mode starts the dispatcher machinery, but your HTTP server must receive
Telegram POST requests and call `onUpdate(rawBody)`.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.builder.webhook
import org.bezsahara.kittybot.bot.onUpdate
import org.bezsahara.kittybot.bot.start
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver

val token = System.getenv("BOT_TOKEN")

val bot = KittyBot<WebhookReceiver> {
    this.token = token

    webhook(
        url = "https://example.com/telegram/$token",
        deletePreviousWebhook = true,
        secretToken = System.getenv("TELEGRAM_WEBHOOK_SECRET")
    )

    dispatchers {
        // Add handlers here.
    }
}

bot.start()

// In your HTTP POST route:
// suspend fun receiveTelegramPayload(rawBody: String) {
//     bot.onUpdate(rawBody)
// }
```

Webhook notes:

- `webhook(...)` calls `deleteWebhook()` first when `deletePreviousWebhook = true`,
  then calls Telegram `setWebhook(...)`.
- Webhook `allowed_updates` are configured through the `webhook(allowedUpdates = ...)`
  argument. `allowUpdatesOf(...)` is used by polling.
- `onUpdate(data)` decodes the raw JSON string into an `Update` and sends it to
  the internal update channel.
- Stop webhook processing with `bot.stop()`.

### Updater Modes

`updaterMode` controls how updates are consumed from the internal channel and
passed into the handler list.

- `UpdaterMode.SingleThread` is the default. It handles updates sequentially and
  is enough for most bots.
- `UpdaterMode.MultiThread(identity, parallelism)` runs several workers. Updates
  with the same non-null identity are processed in order; different identities
  can run in parallel.
- `UpdaterMode.Custom(customUpdater)` lets you provide your own scheduling logic.
  Your `CustomUpdaterSetup.configure(...)` must return immediately and should
  attach launched coroutines to the provided `supervisorJob`.

Built-in `MultiIdentity` choices:

- `MultiIdentity.OfMessageChatIdentity` uses the chat id only for `MessageUpdate`;
  other update kinds return `null`.
- `MultiIdentity.OfAnyUserChatIdentity` uses `update.chatIdOrNull()`.
- `MultiIdentity.OfAnyUserIdentity` uses `update.userIdOrNull()`.
- `MultiIdentity.OfNoneIdentity` returns `null`, so the multi-updater does not
  preserve per-user or per-chat ordering.

Use thread-safe state such as `ConcurrentHashMap` when `MultiThread` can run
different updates at the same time.

`MultiIdentity` now lives in
`org.bezsahara.kittybot.bot.updates.updaters.MultiIdentity`.

### Dispatcher Runtime Configuration

`furballConfig` controls safety limits and a few internal dispatch strategies.
Most bots should keep the default.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.updates.FurballConfig
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver

val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")

    furballConfig = FurballConfig(
        hopSafetyTimes = 3,
        attrsLimit = 1000,
        onRecursionProblem = { hopCount ->
            error("Dispatcher recursion problem after $hopCount jumps")
        },
        ignoreIdentityDuplicated = false,
        multiUpdaterUseMap = false,
        multiUpdaterMapLimit = 50_000
    )
}
```

Runtime config notes:

- `hopSafetyTimes` limits jump recursion relative to the handler-list size.
- `attrsLimit` limits how many `AttrKey`s can be registered in one bot.
- `onRecursionProblem` overrides the default recursion exception behavior.
- `ignoreIdentityDuplicated` disables duplicate handler identity checking.
- `multiUpdaterUseMap` switches the multi-updater shard store from a fixed
  striped array to a hash map.
- `multiUpdaterMapLimit` is the initial cleanup bound for that hash-map mode.

`botContext` is a bot-level `TypeAwareMap` available during setup and runtime.
It is intended for framework-level integrations. For values that belong only to
one update, use `HandlerContext` instead.

### Update Visitor Mode

`dispatchers { ... }` can be replaced with `useUpdatesVisitor(...)` when a bot
wants one visitor object with typed `onMessageUpdate`, `onCallbackQueryUpdate`,
and similar methods. Visitor mode is mutually exclusive with `dispatchers { ... }`.

```kotlin
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.furballs.UpdateVisitor
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

useUpdatesVisitor(object : UpdateVisitor() {
    override suspend fun onMessageUpdate(bot: KittyBot, update: MessageUpdate) {
        // ...
    }
})
```

### Client Configuration

The default client is discovered from `kittybot-client`:
`org.bezsahara.kittybot.telegram.client.BuildVertxClient`.

Use Java's built-in HTTP client without `kittybot-client`:

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.telegram.client.jclient.JavaCustomClient

val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")
    useCustomClient(JavaCustomClient.createDefault())
}
```

Use Ktor by adding Ktor dependencies and providing a client:

```kotlin
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.telegram.client.ktor.KtorCustomClient

val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")
    useCustomClient(KtorCustomClient(HttpClient(CIO)))
}
```

Advanced options:

- Set `apiClientBuilder` for a full custom `ClientBuilder`.
- `useCustomClient(customClient)` adapts a `CustomClient` to the generated API.
- `wrapTheBot { ... }` or `delegatingKittyBot { ... }` can decorate the generated
  `KittyBot`.
- `throwErrorsOnFailure()` wraps the client so Telegram API failures throw instead
  of requiring `.unwrap()` or `.consume()` at each call site.
- Use `createTelegramBot(token)` if you only need the generated Telegram API
  client and do not need the handler system.

### Initialization And Lifetime

Use `init { ... }` for API calls that should run after the client is built and
before update handling starts.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.telegram.classes.bot.BotCommand
import org.bezsahara.kittybot.telegram.classes.bot.BotCommandScopeDefault

val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")

    init {
        deleteMyCommands(BotCommandScopeDefault)
        setMyCommands(
            listOf(
                BotCommand("/start", "Start the bot"),
                BotCommand("/help", "Show help")
            ),
            scope = BotCommandScopeDefault
        ).consume()
    }
}
```

Lifecycle methods:

- Polling: `startPolling(wait = true)`, `startPolling(wait = false)`,
  `stopPolling()`.
- Webhook: `start()`, `stop()`, `onUpdate(rawJson)`.
- `KittyBotConfig.close()` closes the receiver and client builder. The supervisor
  job also closes resources when it completes.

## Core Mechanics

### Main Objects

- `KittyBot<PollingReceiver> { ... }` and `KittyBot<WebhookReceiver> { ... }`
  build a `KittyBotConfig<T>`.
- `KittyBotConfig.kittyBot` is the generated Telegram API client.
- `FelineBuilder` is the configuration DSL receiver.
- `FelineDispatcher` stores the handlers in order.
- `UpdateReceiver` gets updates from polling or webhooks.
- `Furball` is the internal dispatcher runner used by `SingleUpdater`,
  `MultiUpdater`, and `CustomUpdater`.

The update path is:

1. `PollingReceiver` or `onUpdate(...)` produces an `Update`.
2. The update is sent to `KittyBotConfig.updatesChannel`.
3. The configured updater reads from the channel.
4. `Furball.applyHandlers(update)` walks the handler list.
5. Each handler returns a `Decision`.

### Handler Contract

The low-level handler interface is:

```kotlin
fun interface Handler {
    val identity: HandlerIdentity? get() = null
    val allowedKinds: Set<UpdKind>? get() = null

    suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext
    ): Decision
}
```

Important rules:

- Handlers are checked in order of registration.
- `Decision.Next` means "try the next matching handler".
- `Decision.Consumed` means "stop handling this update".
- `Decision.NextTo(identity)` jumps to the handler with that identity.
- `Decision.AfterNextTo(identity)` jumps after the handler with that identity.
- `allowedKinds == null` means the handler accepts all update kinds.
- `allowedKinds == emptySet()` means it accepts no update directly. This is used
  by jump points.
- For raw handlers, always specify `allowedKinds` when possible. It lets the
  dispatcher skip irrelevant handlers without calling them.

The dispatcher precomputes jump tables by `UpdKind`. For `Decision.Next`, it
jumps directly to the next handler that accepts the current update kind. This is
why correct `allowedKinds` matter for speed.

`UpdateKind<T>` used to be generic. New code should import and use `UpdKind`
instead, for example `Set<UpdKind>`. `UpdateKind<T>` remains only as a deprecated
compatibility typealias, and the old top-level `toSet()` helper is also
deprecated; prefer `MessageUpdate.toSet()` or `setOf(MessageUpdate)`.

### Handler Identity

`HandlerIdentity` is a stable integer id used by jump decisions and dynamic
routing. It must be stable for the lifetime of the handler.

Do this:

```kotlin
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update

val targetIdentity = HandlerIdentity.createNew()

val gate = object : Handler {
    override val allowedKinds: Set<UpdKind> = setOf(MessageUpdate)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext
    ): Decision {
        val message = (update as MessageUpdate).message
        return if (message.text == "/jump") {
            Decision.NextTo(targetIdentity)
        } else {
            Decision.Next
        }
    }
}

val target = object : Handler {
    override val identity: HandlerIdentity = targetIdentity
    override val allowedKinds: Set<UpdKind> = setOf(MessageUpdate)

    override suspend fun handleUpdate(
        update: Update,
        bot: KittyBot,
        handlerContext: HandlerContext
    ): Decision {
        val message = (update as MessageUpdate).message
        bot.sendMessage(message.chat.id.toChatId(), "Jump target reached")
        return Decision.Consumed
    }
}
```

Do not return `HandlerIdentity.createNew()` from a custom getter. That creates a
different id on each access and breaks dispatch. Use `HandlerIdentityDelegate`,
`SimpleHandler`, `TypeHandler`, `ensureHasIdentity()`, or a stored `val`.

Duplicate identities are rejected at startup unless
`furballConfig.ignoreIdentityDuplicated = true`.

### Update Kinds And Update Subtypes

`Update` is a sealed class with one concrete subtype per Telegram update field.
Each subtype has a companion object that is also its `UpdKind`. For example,
`MessageUpdate` is both the class name and the update-kind object used in
`setOf(MessageUpdate)`.

Current update kinds include:

- `MessageUpdate` -> `"message"`
- `EditedMessageUpdate` -> `"edited_message"`
- `ChannelPostUpdate` -> `"channel_post"`
- `EditedChannelPostUpdate` -> `"edited_channel_post"`
- `BusinessConnectionUpdate` -> `"business_connection"`
- `BusinessMessageUpdate` -> `"business_message"`
- `EditedBusinessMessageUpdate` -> `"edited_business_message"`
- `DeletedBusinessMessagesUpdate` -> `"deleted_business_messages"`
- `MessageReactionUpdate` -> `"message_reaction"`
- `MessageReactionCountUpdate` -> `"message_reaction_count"`
- `InlineQueryUpdate` -> `"inline_query"`
- `ChosenInlineResultUpdate` -> `"chosen_inline_result"`
- `CallbackQueryUpdate` -> `"callback_query"`
- `ShippingQueryUpdate` -> `"shipping_query"`
- `PreCheckoutQueryUpdate` -> `"pre_checkout_query"`
- `PaidMediaPurchasedUpdate` -> `"purchased_paid_media"`
- `PollUpdate` -> `"poll"`
- `PollAnswerUpdate` -> `"poll_answer"`
- `MyChatMemberUpdate` -> `"my_chat_member"`
- `ChatMemberUpdate` -> `"chat_member"`
- `ChatJoinRequestUpdate` -> `"chat_join_request"`
- `ChatBoostUpdate` -> `"chat_boost"`
- `RemovedChatBoostUpdate` -> `"removed_chat_boost"`
- `ManagedBotUpdate` -> `"managed_bot"`
- `UnknownUpdate` for unknown future Telegram update fields.
- `SyntheticUpdate` for library-created updates such as `GroupedMediaUpdate`.

Use generated cast helpers when you have a raw `Update`:

```kotlin
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull

val message = update.asMessageUpdateOrNull()?.message
```

### Raw Handler Example

Use raw handlers for cases that are not covered by the DSL. Include
`allowedKinds` for speed.

```kotlin
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.addHandler
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

dispatchers {
    addHandler(setOf(MessageUpdate)) { update, bot, _ ->
        val message = (update as MessageUpdate).message
        val text = message.text ?: return@addHandler Decision.Next
        if (!text.startsWith("/raw ")) return@addHandler Decision.Next

        bot.sendMessage(
            message.chat.id.toChatId(),
            "Raw text: ${text.removePrefix("/raw ")}"
        )
        Decision.Consumed
    }
}
```

### HandlerContext

`HandlerContext` is created once per update and shared by all handlers that run
for that update. It is not thread-safe, but a single update's handler chain is
processed in one logical path, so using it inside the chain is safe.

Use it for per-update derived values, not long-lived bot state.

```kotlin
import org.bezsahara.kittybot.bot.action.other.contextHook
import org.bezsahara.kittybot.bot.dispatchers.attrKeyOf
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

dispatchers {
    val normalizedText = attrKeyOf<String>("normalized-text")

    contextHook(setOf(MessageUpdate)) { update, context ->
        context[normalizedText] =
            (update as MessageUpdate).message.text.orEmpty().trim().lowercase()
    }

    text({ it.equals("ping", ignoreCase = true) }) {
        bot.sendMessage(chatId, "normalized=${handlerContext[normalizedText]}")
    }
}
```

Rules for context attributes:

- Create `AttrKey`s before startup with `attrKeyOf<T>()` or `contextValue<T>()`.
- Do not create attribute keys after the bot starts. The `IdentityScope` is closed
  when the dispatcher is prepared.
- Do not use an `AttrKey` from one bot's `IdentityScope` in another bot.
- `handlerContext.emitUpdate(syntheticUpdate)` and `tryEmitUpdate(...)` can push
  synthetic updates back into the update channel.

### Handler Errors

If a handler throws, the configured `HandlerErrorHandler` receives:

- the throwable
- the generated `KittyBot`
- the `Update`
- the `HandlerContext`
- the handler that failed

The error handler returns a `Decision`, so it can consume, continue, or jump. The
default handler prints the stack trace and consumes the update.

### API Results

Telegram API calls return `TResult<T>`.

```kotlin
import org.bezsahara.kittybot.telegram.utils.unwrap

val me = bot.getMe().unwrap()
```

Common result helpers:

- `.consume()` throws if the result is a Telegram error and otherwise ignores the
  value.
- `.unwrap()` returns the value or throws.
- `.unwrapOrNull()` returns `null` on Telegram error.
- `.onSuccess { ... }`, `.onError { ... }`, and `.onResult(...)` are available
  for explicit handling.
- Methods that can return one of two Telegram success types use `TResult.Either`
  and helpers such as `.unwrapFirst()`, `.unwrapSecond()`, and `.expect<T>()`.

If you call `throwErrorsOnFailure()` in the builder, the client is wrapped so API
failures throw automatically.

## DSL Usage

Prefer the DSL because it creates the right scopes and usually sets
`allowedKinds` for you.

### Basic Handler DSL

Available common handlers:

- `command("/name", description = null, addToBotCommands = false) { ... }`
- `text("exact") { ... }`
- `text(regex) { ... }`
- `text { ... }` for any text message
- `text(check: (String) -> Boolean) { ... }`
- `callbackQuery({ CallbackQuery -> Boolean }) { ... }`
- `contact { ... }`
- `messageHandler({ message, context -> Boolean }) { ... }`
- `handler(check, updateKinds = setOf(...)) { ... }`
- `handleTypeOf<SomeUpdate>({ update -> Boolean }) { ... }`
- `mediaGroup(...) { ... }` after `setupMediaGroupHandler(...)`

The main scopes:

- `HandlerScope<T : Update>` exposes `bot`, `update`, and `handlerContext`.
- `MessageScope` extends `HandlerScope<MessageUpdate>` and adds `message`.
- `CommandScope` extends `MessageScope` and adds `commandArgs`.
- `CallScope` extends `HandlerScope<CallbackQueryUpdate>` and adds
  `callbackQuery`.
- `UpdateScope` is used by raw `handler(...)`.
- Many specialized scopes inherit from `HandlerScope`, so the same mental model
  applies across the DSL.

`chatId` is an extension property for `HandlerScope<MessageUpdate>` and
`HandlerScope<CallbackQueryUpdate>`. For callback queries it is the user id from
`callbackQuery.from.id`; if you need the message chat, read
`callbackQuery.message?.chat?.id`.

### Commands, Text, Callback Queries, Keyboards

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.y.callbackQuery
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.telegram.classes.chat.toChatId
import org.bezsahara.kittybot.telegram.utils.key.buildInlineKeyboardMarkup

fun main() {
    val bot = KittyBot<PollingReceiver> {
        token = System.getenv("BOT_TOKEN")

        dispatchers {
            command("/start", "Start the bot", addToBotCommands = true) {
                bot.sendMessage(
                    chatId,
                    "Choose an action",
                    replyMarkup = buildInlineKeyboardMarkup {
                        callback("Ping", "menu:ping")
                        url("Telegram", "https://telegram.org")
                    }
                )
            }

            command("/echo") {
                bot.sendMessage(chatId, commandArgs ?: "Nothing to echo")
            }

            text("hi") {
                bot.sendMessage(chatId, "Hi")
            }

            callbackQuery({ it.data == "menu:ping" }) {
                bot.answerCallbackQuery(callbackQuery.id, "pong")
                callbackQuery.message?.chat?.id?.toChatId()?.let { originChat ->
                    bot.sendMessage(originChat, "Callback handled")
                }
            }
        }
    }

    bot.startPolling()
}
```

Command rules:

- The command string must start with `/`.
- It must contain 1..32 Latin letters, digits, or underscores after `/`.
- `commandArgs` is `null` if no arguments were sent.
- A command matches exactly `/cmd` or `/cmd args`; `/cmdsuffix` is not accepted.
- If `addToBotCommands = true` and a description is provided, the builder queues
  the command for `setMyCommands` during initialization.

### Type-Specific Handlers

Use `handleTypeOf<T>()` when you want a typed update scope without writing a raw
handler.

```kotlin
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.handleTypeOf
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

fun FelineDispatcher.registerTypeHandler() {
    handleTypeOf<MessageUpdate>({ it.message.text == "cookies" }) {
        bot.sendMessage(chatId, "Cookies received")
    }
}
```

### State Handler

Use `stateHandler` when an external state store decides which group of handlers
should process an update.

```kotlin
import org.bezsahara.kittybot.bot.action.flow.stateHandler
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import java.util.concurrent.ConcurrentHashMap

sealed interface ProfileState {
    data object WaitingName : ProfileState
    data class Active(val name: String) : ProfileState
}

fun TransparentHandlerStore.profileStateHandlers() {
    val states = ConcurrentHashMap<Long, ProfileState>()

    stateHandler({ update, _ ->
        val message = update.message ?: return@stateHandler null
        states[message.chat.id]
    }, setOf(MessageUpdate)) {
        valueOf(ProfileState.WaitingName) {
            text {
                states[message.chat.id] =
                    ProfileState.Active(message.text ?: "Unknown")
                bot.sendMessage(chatId, "Saved")
            }
        }

        instanceOf<ProfileState.Active> {
            text("/profile") {
                bot.sendMessage(chatId, "Hello ${handlerContext.state().name}")
            }
        }

        default {
            text {
                bot.sendMessage(chatId, "No state-specific handler matched")
            }
        }
    }
}
```

State handler branch methods:

- `valueOf(value)` uses `equals`.
- `classOf<T>()` matches exactly the runtime class.
- `instanceOf<T>()` matches subclasses too.
- `check { ... }` uses a custom predicate.
- `default { ... }` works like an `else` branch.

Branches are tested in declaration order. If the state finder returns `null`, the
state handler is skipped.

### Flow Handler

Use `flowHandler` for section-based flows where an identity, usually a chat id,
progresses through numbered or named sections.

```kotlin
import org.bezsahara.kittybot.bot.action.flow.flowHandler
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

fun FelineDispatcher.wizardFlow() {
    flowHandler<String>(
        flowIdentityFinder = { update, _ -> update.message?.chat?.id?.toString() },
        updateKinds = setOf(MessageUpdate)
    ) {
        section {
            command("/wizard") {
                handlerContext.pauseSection("started")
                bot.sendMessage(chatId, "Send step 1")
            }

            text("step 1") {
                handlerContext.nextSection("step 1 complete")
                bot.sendMessage(chatId, "Now send secret")
            }
        }

        section("secret-section") {
            command("/reset") {
                handlerContext.resetFlow()
                bot.sendMessage(chatId, "Reset")
            }

            text("secret") {
                val previous = handlerContext.getFlowArgs() as? String
                handlerContext.nextSection(previous)
                bot.sendMessage(chatId, "Unlocked")
            }
        }

        section("done") {
            text {
                bot.sendMessage(chatId, "Done: ${handlerContext.getFlowArgs()}")
            }
        }
    }
}
```

Inside a flow section, `handlerContext` gets helper extensions:

- `nextSection(args = null)`
- `nextSectionWithName(name, args = null)`
- `nextSectionWithId(id, args = null)`
- `pauseSection(args = null)`
- `resetFlow()`
- `getFlowArgs()`
- `getFlowIdentityData()`

The default storage is in-memory. Provide your own `FlowIdentityStorage<T>` if
flow position must survive restarts or be shared between processes.

### Conversations

Conversations are coroutine-based. A conversation entry point starts a coroutine,
then `receive*().await()` waits for future updates from the same chat or same
user, depending on the catcher.

```kotlin
import org.bezsahara.kittybot.bot.conv.buildConversation
import org.bezsahara.kittybot.bot.conv.cts.receivePhotos
import org.bezsahara.kittybot.bot.conv.cts.receiveText
import org.bezsahara.kittybot.bot.conv.scope.onText
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.telegram.client.file.TelegramFile

fun FelineDispatcher.signupConversation() {
    buildConversation {
        onText("/signup") {
            bot.sendMessage(chatId, "What is your name?")
            val name = receiveText().await()

            bot.sendMessage(chatId, "Send a profile photo, $name")
            val photos = receivePhotos().await()

            bot.sendPhoto(
                chatId,
                TelegramFile.withId(photos.maxBy { it.height }.fileId),
                caption = "Saved"
            )
        }
    }
}
```

Conversation helpers:

- Entry points: `onStartCommand`, `onText(text)`, `onText(regex)`,
  `onText(check)`, `onMsg(check)`, or `on(customBuilder)`.
- Waiters: `receiveMessage`, `receiveCommand`, `receiveText`,
  `receiveAnimation`, `receiveAudio`, `receiveDocument`, `receivePhotos`,
  `receiveSticker`, `receiveVideo`, `receiveVideoNote`, `receiveVoice`,
  `receiveContact`, `receiveDice`, `receiveGame`, `receivePoll`,
  `receiveVenue`, `receiveLocation`, `receiveInvoice`, `receiveCallbackQuery`,
  `receiveCallbackData`, `receiveCallbackMessage`,
  `receiveCallbackInlineMessageId`, `receiveCallbackGameShortName`.
- `OnMsgScope` is also a `CoroutineScope`; you can `launch { ... }` background
  waiters.
- `endConversation()` cancels the conversation coroutine.
- Only one conversation can be active for a chat at a time in the built-in
  runtime.

### Routing

Use routing when a key should jump directly into a section of handlers. This is
useful for large bots because unrelated section handlers are skipped.

```kotlin
import org.bezsahara.kittybot.bot.action.route.routingInt
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.dispatchers.y.text
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull
import java.util.concurrent.ConcurrentHashMap

fun FelineDispatcher.routeByChat() {
    val routeByChat = ConcurrentHashMap<Long, Int>()

    routingInt({ update, _ ->
        val chatId = update.asMessageUpdateOrNull()?.message?.chat?.id
            ?: return@routingInt Int.MIN_VALUE
        routeByChat[chatId] ?: Int.MIN_VALUE
    }) {
        section(1) {
            text {
                bot.sendMessage(chatId, "Route 1")
            }
        }

        section(2) {
            text {
                bot.sendMessage(chatId, "Route 2")
            }
        }

        default {
            text {
                bot.sendMessage(chatId, "Unknown route")
            }
        }
    }
}
```

Routing choices:

- `routingInt { ... }` uses `Int.MIN_VALUE` to mean "no key".
- `routing<T> { ... }` uses `null` to mean "no key".
- A missing key goes to `default` if it exists; otherwise it is an error.
- `common(handler)` defines a handler that runs before any selected section.

### Callback Query Route

Use `callbackQueryRoute` for simple callback data maps.

```kotlin
import org.bezsahara.kittybot.bot.action.route.callbackQueryRoute
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher

fun FelineDispatcher.callbackRoutes() {
    val route = callbackQueryRoute(selector = { update, _ ->
        update.callbackQuery.data?.substringAfter("action:", missingDelimiterValue = "")
    })

    route.callbackQuery("save") {
        bot.answerCallbackQuery(callbackQuery.id, "Saved")
    }

    route.callbackQuery("delete") {
        bot.answerCallbackQuery(callbackQuery.id, "Deleted")
    }

    route.default {
        bot.answerCallbackQuery(callbackQuery.id, "Unknown action")
    }
}
```

### Media Groups

There are two media group styles.

Sequential transformer style:

```kotlin
import org.bezsahara.kittybot.bot.action.mgroup.setupMediaGroupHandler
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.mediaGroup

fun FelineDispatcher.mediaGroups() {
    setupMediaGroupHandler(periodLimitMillis = 800)

    mediaGroup({ messages -> messages.isNotEmpty() }) {
        bot.sendMessage(chatId, "Got ${update.mediaMessagesGrouped.size} media items")
    }
}
```

This requires updater ordering by chat: `SingleThread` or `MultiThread` with
`MultiIdentity.OfAnyUserChatIdentity` or `MultiIdentity.OfMessageChatIdentity`.
Pass `ignoreSequentialSafety = true` only if you know your updater preserves the
needed order.

Direct accumulator style:

```kotlin
import org.bezsahara.kittybot.bot.action.mgroup.MediaGroupCheck
import org.bezsahara.kittybot.bot.action.mgroup.mediaGroupHandler
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.telegram.classes.chat.toChatId

fun FelineDispatcher.mediaGroupAccumulator() {
    mediaGroupHandler(MediaGroupCheck.OfAnyGroupId) {
        bot.sendMessage(message.chat.id.toChatId(), "Got ${mediaGroup.size} media items")
    }
}
```

The accumulator collects `MessageUpdate`s by `(chatId, mediaGroupId)` and calls
the block after an inactivity window.

### Command Groups

`commandGroupHandler` maps slash commands to annotated Kotlin methods.

```kotlin
import org.bezsahara.kittybot.bot.action.comms.BotCommand
import org.bezsahara.kittybot.bot.action.comms.CommandGroup
import org.bezsahara.kittybot.bot.action.comms.ExecBlock
import org.bezsahara.kittybot.bot.action.comms.commandGroupHandler
import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId

class AdminCommands : CommandGroup {
    @BotCommand("Replies with pong")
    fun ping(): ExecBlock = ExecBlock {
        bot.sendMessage(chatId, "pong")
    }

    @BotCommand("Greets a user")
    fun greet(name: String): ExecBlock = ExecBlock {
        bot.sendMessage(chatId, "Hello $name")
    }
}

fun HandlerStore.adminCommands() {
    commandGroupHandler(AdminCommands())
}
```

Supported argument types are `String`, `Long`, `Double`, and `List` of supported
types. Use `twoStepCommandGroupHandler(...)` when the command can be sent first
and its arguments in a later message.

### Dynamic Handlers

Use dynamic handlers when handlers must be added or removed while the bot is
running.

```kotlin
import org.bezsahara.kittybot.bot.action.dyn.createDynamicHandlerStore
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.telegram.classes.core.update.asMessageUpdateOrNull

fun FelineDispatcher.dynamicExample() {
    val registry = createDynamicHandlerStore()

    val handler = Handler { update, bot, _ ->
        val message = update.asMessageUpdateOrNull()?.message
            ?: return@Handler Decision.Next
        if (message.text != "dynamic") return@Handler Decision.Next

        bot.sendMessage(update.chatIdOrNull()!!, "Dynamic handler response")
        Decision.Consumed
    }

    registry.addHandler(handler)
    // Later:
    // registry.removeHandler(handler)
}
```

The registry publishes immutable snapshots for readers, so add/remove operations
are safe while updates are being handled.
`collectAndAdd { ... }` is available when a setup block should return the exact
handlers it added so they can be removed later with `removeHandlers(...)`.

### Filters And Control Helpers

Useful dispatcher helpers:

- `filters { addFilter(handler) }` inserts handlers at the beginning of the
  handler list.
- `addHandlerFirst(handler)` inserts a handler at the front.
- `messageRateFilter(timePeriodMillis) { ... }` filters frequent messages per
  chat.
- `testEach { ... }` runs all handlers in the block even if one returns
  `Consumed`; after the block it consumes if any handler consumed.
- `scopeOfType(MessageUpdate, CallbackQueryUpdate) { ... }` forces added
  handlers to those update kinds.
- `contextHook { update, context -> ... }` precomputes context data and returns
  `Decision.Next`.
- `debugHook { ... }` is like a non-consuming scoped handler.
- `replayUpdates(...)` can replace an update by sending another update into the
  channel and consuming the current one.
- `consumeUpdatesInChannel(...)` exposes matching updates through a coroutine
  `Channel`.
- `handleUnknownUpdate { ... }` handles `UnknownUpdate` for future Telegram
  compatibility.

### Telegram Object Builders

These are not dispatchers, but they are common in handler code.

Inline keyboards:

```kotlin
import org.bezsahara.kittybot.telegram.utils.key.buildInlineKeyboardMarkup

val markup = buildInlineKeyboardMarkup {
    callback("Save", "action:save")
    callback("Delete", "action:delete")
    addRow {
        url("Docs", "https://core.telegram.org/bots/api")
    }
}
```

Reply keyboards:

```kotlin
import org.bezsahara.kittybot.telegram.utils.key.buildReplyKeyboardMarkup

val replyMarkup = buildReplyKeyboardMarkup(resizeKeyboard = true) {
    text("Yes")
    text("No")
}
```

Bot commands:

```kotlin
import org.bezsahara.kittybot.telegram.utils.setMyCommands

init {
    setMyCommands {
        command("/start", "Start")
        command("/help", "Help")
        allPrivateChats {
            command("/private", "Private-only command")
        }
    }
}
```

Message entities:

```kotlin
import org.bezsahara.kittybot.telegram.utils.entity.Bold
import org.bezsahara.kittybot.telegram.utils.entity.Italic
import org.bezsahara.kittybot.telegram.utils.entity.buildEntityString
import org.bezsahara.kittybot.telegram.utils.entity.sendMessage

val entityText = buildEntityString {
    Bold {
        addString("Important")
    }
    addString(": ")
    Italic {
        addString("read this")
    }
}

bot.sendMessage(chatId, entityText)
```

### Agent Guidelines

- Before guessing an API, try to find the library sources locally. Start from the
  user's build system: inspect Gradle/Maven dependencies, included builds,
  composite builds, local project modules, source jars in dependency caches, or
  IDE "external libraries" paths. Prefer local source and KDoc.
  For API methods it is better to read the abstract class of org.bezsahara.kittybot.bot.KittyBot,
  it has documentation for every single method.
- Prefer `command`, `text`, `callbackQuery`, `handleTypeOf`, `stateHandler`,
  `flowHandler`, `routing`, and `buildConversation` before writing raw handlers.
- If you write a raw handler, set `allowedKinds`.
- If you add jump logic, use stable `HandlerIdentity` values.
- Keep persistent bot state outside `HandlerContext`; use `HandlerContext` only
  for one update's processing chain.
- With `UpdaterMode.MultiThread`, make shared state thread-safe.
- Use `allowUpdatesOf(...)` for polling bots to avoid receiving unused update
  types from Telegram.
- Use `setErrorHandler` for production bots. The default behavior only prints
  and consumes.
- Remember that Telegram API methods return `TResult`; call `.consume()`,
  `.unwrap()`, or handle errors explicitly.
