# TelegramKitty Guide for Coding Agents

Use this guide when writing Kotlin code that depends on TelegramKitty. It targets
TelegramKitty `5.0.0`, Telegram Bot API 10.1, and JVM 17.

TelegramKitty has two published modules:

- `kittybot` contains the generated Telegram models and API surface, bot
  lifecycle, dispatcher runtime, DSLs, and custom-client SPI.
- `kittybot-client` provides the default Vert.x HTTP transport and related
  helpers.

## Source-first lookup

Do not guess TelegramKitty signatures from memory or reproduce generated API
documentation in project code.

Use this lookup order:

1. Inspect the dependency version in Gradle or Maven.
2. Prefer a local checkout or the Maven sources JAR for that exact version.
3. Read the focused files in `docs/agents/` when they are available.
4. Use GitHub only as a fallback. The default branch can be newer than the
   installed dependency.

Online fallbacks:

- [TelegramKitty repository](https://github.com/bezsahara/TelegramKitty)
- [Agent documentation on GitHub](https://github.com/bezsahara/TelegramKitty/tree/master/docs/agents)

For generated Telegram methods, read
`kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/KittyBot.kt`. It contains
the public signatures and KDoc. Do not load all of
`kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/client/TCustomClient.kt`
or
`kittybot-client/src/main/kotlin/org/bezsahara/kittybot/telegram/client/TApiClient.kt`
merely to discover a method signature; both are large generated implementations.

## Topic routing

Read only the file needed for the current task:

| Task | Guide |
|---|---|
| Dependencies, polling, webhooks, clients, startup, shutdown | [Setup and lifecycle](docs/agents/setup-and-lifecycle.md) |
| Handlers, decisions, update kinds, context, identities, concurrency | [Dispatch runtime](docs/agents/dispatch-runtime.md) |
| Commands, text, callbacks, raw handlers, filters, builders | [Handler DSL](docs/agents/handler-dsl.md) |
| State, flows, routing, conversations, media and command groups | [Flows and conversations](docs/agents/flows-and-conversations.md) |

## Source map

Use exact source files when a focused guide is insufficient:

| Need | Source |
|---|---|
| Generated Telegram API methods and KDoc | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/KittyBot.kt` |
| API results such as `TResult` and `unwrap` | `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/utils/TResult.kt` |
| Bot builder and updater modes | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/builder/builder.kt` |
| Polling/webhook lifetime | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/KittyBotConfig.kt` |
| Handler and `Decision` contracts | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/dispatchers/Handler.kt` |
| Handler DSL implementations | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/dispatchers/y/` |
| Per-update context | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/HandlerContext.kt` |
| Update kinds and generated update subtypes | `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/classes/core/update/` |
| Flow, state, routing, and dynamic handlers | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/` |
| High-level conversations | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/conv/` |
| Lightweight manually managed conversations | `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/conv2/` |
| Generated Telegram models | `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/classes/` |
| Rich-message usage | `samples/src/main/kotlin/RichTextExample.kt` |
| Custom HTTP-client SPI | `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/client/CustomClient.kt` |
| Default Vert.x client | `kittybot-client/src/main/kotlin/org/bezsahara/kittybot/telegram/client/BuildVertxClient.kt` |

## Dependencies

For the normal Vert.x setup:

```kotlin
dependencies {
    implementation("org.bezsahara:kittybot:5.0.0")
    implementation("org.bezsahara:kittybot-client:5.0.0")
}
```

Use only `kittybot` when providing a `CustomClient` or `ClientBuilder`.

## Minimal polling bot

`KittyBot<PollingReceiver> { ... }` returns
`KittyBotConfig<PollingReceiver>`. The generated API client is
`config.kittyBot` and is exposed as `bot` inside handler scopes.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver

fun main() {
    val config = KittyBot<PollingReceiver> {
        token = System.getenv("BOT_TOKEN")
        ensureOnlyNewUpdates()

        dispatchers {
            command("/start") {
                bot.sendMessage(chatId, "Hi from TelegramKitty")
            }
        }
    }

    config.startPolling()
}
```

`startPolling()` blocks by default. Use `startPolling(wait = false)` when the
caller will keep the returned `Job` or otherwise keep the process alive.

## Rules that prevent common mistakes

- Telegram API methods are generated `suspend` functions returning
  `TResult<T>`. Use `unwrap()` when the value is required, `consume()` when only
  success matters, or handle `onResult`/`onError` explicitly.
- `throwErrorsOnFailure()` wraps the client so Telegram failures throw
  automatically.
- Register Telegram's command menu explicitly with `setMyCommands`. The
  `command(command, description, addToBotCommands)` overload is deprecated;
  use `command("/name") { ... }` for dispatch.
- `ensureOnlyNewUpdates()` is the current stale-update helper. The file/custom
  recovery overloads are deprecated for removal.
- Prefer the handler DSL. For a raw `Handler`, set `allowedKinds` whenever
  possible.
- `Decision.Next` continues to the next matching handler;
  `Decision.Consumed` stops propagation.
- Keep `HandlerIdentity` stable. Never create a new identity from a getter.
- `HandlerContext` belongs to one update-processing chain. Keep persistent
  state elsewhere.
- Create `AttrKey` values during setup and do not share keys between bots.
- With `UpdaterMode.MultiThread`, make shared state thread-safe and choose a
  `MultiIdentity` that preserves the ordering the feature needs.
- `dispatchers { ... }` and `useUpdatesVisitor(...)` are mutually exclusive.
- Register narrower update subscriptions with `allowUpdatesOf(...)` for
  polling. Webhook subscriptions belong in `webhook(allowedUpdates = ...)`.
- Configure a production `setErrorHandler`. The default prints the exception
  and consumes the update.

## Current feature locations

- The optimized immutable and mutable update-kind sets are in
  `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/UpdKindSet.kt` and
  `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/UpdKindMutSet.kt`.
  Normal code can continue using `Set<UpdKind>` and `updKindSetOf(...)`.
- The DSL includes a `document { ... }` handler in
  `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/dispatchers/y/Other.kt`.
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/conv2/` provides
  lightweight conversations whose jobs and matching rules are managed
  explicitly.
- Rich-message request/response models are in
  `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/classes/rich/`; use
  `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/KittyBot.kt` for method
  signatures and `samples/src/main/kotlin/RichTextExample.kt` for an end-to-end
  example.
- Entity and inline-keyboard builders live under
  `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/utils/entity/` and
  `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/utils/key/`.
