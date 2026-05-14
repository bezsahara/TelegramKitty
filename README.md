![logo](logo.png)

# TelegramKitty
TelegramKitty is a Kotlin-first Telegram Bot API wrapper for fast JVM bots.
It gives you generated Telegram methods and models, a coroutine-friendly handler DSL,
predictable update processing, and a pluggable HTTP layer.

Current generated API target: **Telegram Bot API 10**.

And yes, it can send [cat pictures](#cats).

Project layout:

- `kittybot` - generated Telegram API, core bot logic, dispatcher DSL, custom client SPI
- `kittybot-client` - default Vert.x HTTP client, file helpers, and cat helpers
- `samples` - runnable examples

## For AI Agents

This repository includes [FOR_AGENTS.md](FOR_AGENTS.md), an extensive usage guide
for AI coding agents and tool-using assistants. It explains configuration,
dispatcher mechanics, handler identity, update kinds, and the preferred DSL
patterns with imports and code snippets.

When an agent uses TelegramKitty in another project, it should first try to find
the local library sources through the user's build system, dependency cache,
source jars, included builds, or IDE external library paths before guessing APIs.

## Features

- Generated Telegram Bot API methods and serializable Telegram types with official docs in KDoc
- Simple polling and webhook setup
- Handler DSL for commands, text, callback queries, contacts, media groups, and raw update types
- Conversation API for interactive multi-step flows
- `flowHandler`, routing, command groups, dynamic handlers, filters, guards, and context attributes
- Single-thread, ordered multi-thread, and custom update processing
- Allowed-update filtering and stale-update skipping with `ensureOnlyNewUpdates(...)`
- Inline keyboard, reply keyboard, bot command, and message entity builders
- File download helpers when using the default Vert.x client
- Custom HTTP clients through `CustomClient`, with Ktor and Java `HttpClient` implementations available
- Built-in cat helpers via `sendCatPicture(...)`, `sendTheCatApi(...)`, `sendHttpCat(...)`, and `sendTextCat()`

## Installation

Use the default Vert.x client:

```kotlin
dependencies {
    implementation("org.bezsahara:kittybot:4.0.0")
    implementation("org.bezsahara:kittybot-client:4.0.0")
}
```

If you want to provide your own HTTP client, `kittybot-client` is not required:

```kotlin
dependencies {
    implementation("org.bezsahara:kittybot:4.0.0")
}
```

## Quick Start

```kotlin
val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")
    ensureOnlyNewUpdates()

    dispatchers {
        command("/start") {
            bot.sendMessage(chatId, "Hi from TelegramKitty")
        }
    }
}

bot.startPolling()
```

## Conversations

```kotlin
val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")
    ensureOnlyNewUpdates()

    dispatchers {
        buildConversation {
            onStartCommand {
                bot.sendMessage(chatId, "Hi! What's your name?")
                val name = receiveText().await()

                bot.sendMessage(chatId, "Send a picture, $name")
                val picture = receivePhotos().await()
            }
        }
    }
}

bot.startPolling()
```

## State Handler

Use `stateHandler(...)` when updates should be routed by your own per-chat or per-user state.

```kotlin
sealed interface ProfileState {
    object WaitingName : ProfileState
    class Active(val name: String) : ProfileState
}

val states = ConcurrentHashMap<Long, ProfileState>()

dispatchers {
    stateHandler({ update, _ ->
        val message = update.message ?: return@stateHandler null
        states[message.chat.id]
    }) {
        valueOf(ProfileState.WaitingName) {
            text {
                states[message.chat.id] = ProfileState.Active(message.text ?: "Unknown")
                bot.sendMessage(chatId, "Saved")
            }
        }

        instanceOf<ProfileState.Active> {
            text("/profile") {
                bot.sendMessage(chatId, "Hello ${handlerContext.state().name}")
            }
        }
    }
}
```

## Webhooks

```kotlin
val token = System.getenv("BOT_TOKEN")

val bot = KittyBot<WebhookReceiver> {
    this.token = token

    webhook(
        url = "https://example.com/telegram/$token",
        deletePreviousWebhook = true
    )

    dispatchers {
        text("/start") {
            bot.sendMessage(chatId, "Hi from webhook mode")
        }
    }
}

bot.start()

// Pass raw Telegram webhook payloads from your HTTP server:
// bot.onUpdate(payload)
```

## Custom Clients

Use `kittybot-client` for the default Vert.x transport. If you want another HTTP engine,
use `useCustomClient(...)` or set a full `ClientBuilder` through `apiClientBuilder`.

Java's built-in `HttpClient` works without extra dependencies:

```kotlin
val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")
    useCustomClient(JavaCustomClient.createDefault())
}
```

Ktor is also supported. Add your own Ktor client dependencies and engine:

```kotlin
dependencies {
    implementation("org.bezsahara:kittybot:4.0.0")
    implementation("io.ktor:ktor-client-core:3.4.0")
    implementation("io.ktor:ktor-client-cio:3.4.0")
}
```

```kotlin
val bot = KittyBot<PollingReceiver> {
    token = System.getenv("BOT_TOKEN")
    useCustomClient(KtorCustomClient(HttpClient(CIO)))

    dispatchers {
        text("/start") {
            bot.sendMessage(chatId, "Running on a custom client")
        }
    }
}
```

The `CustomClient` SPI receives absolute Telegram method URLs, so implementations only need to send requests and return raw responses.

## Update Processing

The default updater mode is `UpdaterMode.SingleThread`. You can also switch to:

- `UpdaterMode.MultiThread(...)`
- `UpdaterMode.Custom(...)`

For example:

```kotlin
updaterMode = UpdaterMode.MultiThread(
    MultiIdentity.OfAnyUserChatIdentity,
    parallelism = 32
)
```

This lets different chats/users run in parallel while preserving order for the same identity.

## Just The API Client

If you only need Telegram API calls without the handler system, use `createTelegramBot(...)`.

```kotlin
val bot = createTelegramBot(System.getenv("BOT_TOKEN"))
val me = bot.getMe().unwrap()
```

## Cats

Cat helpers live in [cats.kt](kittybot-client/src/main/kotlin/org/bezsahara/kittybot/bot/cats.kt) and are available when you use the default Vert.x client module.

```kotlin
text("/cat") {
    bot.sendCatPicture(chatId)
}

text("/cat_says") {
    bot.sendCatPicture(chatId, "TelegramKitty")
}

text("/httpcat") {
    bot.sendHttpCat(chatId, 404)
}
```

## More Examples

See [samples](samples/src/main/kotlin) for runnable examples:

- [polling.kt](samples/src/main/kotlin/polling.kt) and [webhook.kt](samples/src/main/kotlin/webhook.kt)
- [ConversationBuilder.kt](samples/src/main/kotlin/ConversationBuilder.kt)
- [FlowExample.kt](samples/src/main/kotlin/FlowExample.kt)
- [StateHandlerExample.kt](samples/src/main/kotlin/StateHandlerExample.kt)
- [RoutingExample.kt](samples/src/main/kotlin/RoutingExample.kt)
- [DynamicHandlersExample.kt](samples/src/main/kotlin/DynamicHandlersExample.kt)
- [CommandGroupExample.kt](samples/src/main/kotlin/CommandGroupExample.kt)
- [FilesExample.kt](samples/src/main/kotlin/FilesExample.kt)
- [MediaGroupExample.kt](samples/src/main/kotlin/MediaGroupExample.kt)

You can also check out [KittyChat](https://github.com/bezsahara/KittyChat) project.
It uses this library for a simple AI telegram bot for conversations.

# License
Copyright 2026 Hlib Korol

Permission is hereby granted, free of charge,
to any person obtaining a copy of this software and
associated documentation files (the "Software"),
to deal in the Software without restriction,
including without limitation the rights to use, copy, modify,
merge, publish, distribute, sublicense, and/or
sell copies of the Software, and to permit persons
to whom the Software is furnished to do so, subject
to the following conditions:

The above copyright notice and this permission notice
shall be included in all copies or substantial portions
of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
