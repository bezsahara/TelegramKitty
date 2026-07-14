# Setup and Lifecycle

Read this file for dependencies, client selection, polling, webhooks, startup,
and shutdown.

## Source pointers

- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/builder/builder.kt`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/KittyBotConfig.kt`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/builder/JustBotBuilder.kt`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/receiver/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/client/CustomClient.kt`
- `kittybot-client/src/main/kotlin/org/bezsahara/kittybot/telegram/client/BuildVertxClient.kt`

## Dependencies

TelegramKitty 5.0.0 targets JVM 17.

Use both modules for the default Vert.x transport:

```kotlin
dependencies {
    implementation("org.bezsahara:kittybot:5.0.0")
    implementation("org.bezsahara:kittybot-client:5.0.0")
}
```

Use only `kittybot` when supplying `useCustomClient(...)` or
`apiClientBuilder`.

## Polling

`KittyBot<PollingReceiver>` creates a `KittyBotConfig`, not the generated API
client. Use `config.kittyBot` outside handlers and `bot` inside handler scopes.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.builder.UpdaterMode
import org.bezsahara.kittybot.bot.builder.pollingTimeout
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.bot.startPolling
import org.bezsahara.kittybot.bot.updates.receiver.PollingReceiver
import org.bezsahara.kittybot.bot.updates.updaters.MultiIdentity
import org.bezsahara.kittybot.telegram.classes.core.update.CallbackQueryUpdate
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate

val config = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")
    pollingTimeout = 60

    allowUpdatesOf(MessageUpdate, CallbackQueryUpdate)
    ensureOnlyNewUpdates()

    updaterMode = UpdaterMode.MultiThread(
        MultiIdentity.OfAnyUserChatIdentity,
        parallelism = 16
    )

    dispatchers {
        command("/ping") {
            bot.sendMessage(chatId, "pong")
        }
    }
}

config.startPolling()
```

Polling facts:

- `startPolling()` blocks by default and returns the polling `Job`.
- `startPolling(wait = false)` returns immediately.
- `stopPolling()` cancels the bot supervisor job.
- `pollingTimeout` is in seconds and exists only for `PollingReceiver`.
- `allowUpdatesOf(...)` becomes Telegram's `allowed_updates` for polling.
- `ensureOnlyNewUpdates(tries, timeout, kinds)` drains pending updates during
  initialization. Its defaults are `tries = 2`, `timeout = 0`, and all Telegram
  update kinds.
- `ensureOnlyNewUpdatesWithFile`, `ensureOnlyNewUpdatesCustom`, and the callback
  recovery overload are deprecated for removal. Do not introduce them into new
  code.
- If a webhook may already exist, call `deleteWebhook()` from an `init { ... }`
  block before polling.

## Webhooks

TelegramKitty does not start an HTTP server. Receive Telegram's POST body in the
chosen server and pass the raw JSON to `onUpdate(...)`.

```kotlin
import org.bezsahara.kittybot.bot.builder.KittyBot
import org.bezsahara.kittybot.bot.builder.webhook
import org.bezsahara.kittybot.bot.onUpdate
import org.bezsahara.kittybot.bot.start
import org.bezsahara.kittybot.bot.updates.receiver.WebhookReceiver

val config = KittyBot<WebhookReceiver> {
    token = System.getenv("BOT_TOKEN")

    webhook(
        url = "https://example.com/telegram",
        secretToken = System.getenv("TELEGRAM_WEBHOOK_SECRET"),
        deletePreviousWebhook = true
    )

    dispatchers {
        // handlers
    }
}

config.start()

// Inside the HTTP POST route:
// config.onUpdate(rawRequestBody)
```

`webhook(...)` optionally deletes the previous webhook, then calls
`setWebhook(...)` during initialization. Put webhook subscriptions in its
`allowedUpdates` argument. Validate Telegram's secret-token header in the HTTP
server before calling `onUpdate`.

Use `stop()` to cancel webhook processing.

## Update scheduling

- `UpdaterMode.SingleThread` is the default and preserves global order.
- `UpdaterMode.MultiThread(identity, parallelism)` processes identities in
  parallel while preserving order for the same non-null identity.
- `UpdaterMode.Custom(customUpdater)` delegates scheduling to a
  `CustomUpdaterSetup`.

Built-in `MultiIdentity` values are
`OfMessageChatIdentity`, `OfAnyUserChatIdentity`, `OfAnyUserIdentity`, and
`OfNoneIdentity`. Shared state must be thread-safe with `MultiThread`.

## Clients

The default `ClientBuilder` is discovered from `kittybot-client`.

Alternatives in `kittybot` include:

- `JavaCustomClient.createDefault()` for Java's HTTP client
- `KtorCustomClient(HttpClient(...))` when Ktor dependencies are supplied
- a custom `CustomClient` passed to `useCustomClient(...)`
- a complete custom `ClientBuilder` assigned to `apiClientBuilder`

Use `createTelegramBot(token)` when only the generated Telegram API client is
needed. It returns a `CreatedBot`; close it when finished.

## Initialization and lifetime

`init { ... }` runs suspending Telegram API setup after the client is built and
before updates start. It is the right place for `setMyCommands`,
`deleteWebhook`, and similar calls.

`KittyBotConfig.close()` closes the receiver and client builder. Completion of
the supervisor job also closes them.
