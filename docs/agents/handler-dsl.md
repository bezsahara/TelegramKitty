# Handler DSL

Read this file for commands, text, callbacks, common typed handlers, raw
handlers, filters, and Telegram object builders.

## Source pointers

- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/dispatchers/y/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/dispatchers/y/scopes/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/other/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/each/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/utils/key/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/utils/entity/`

## Common handlers

Prefer the DSL because it creates the correct scope and usually supplies
`allowedKinds`.

- `command("/name") { ... }`
- `text("exact") { ... }`
- `text(regex) { ... }`
- `text { ... }`
- `text({ value -> predicate }) { ... }`
- `callbackQuery(predicate) { ... }`
- `contact { ... }`
- `document { ... }`
- `messageHandler(...) { ... }`
- `handleTypeOf<UpdateSubtype>(...) { ... }`
- `handler(check, updateKinds) { ... }`

Common scopes expose `bot`, `update`, and `handlerContext`.
`MessageScope` adds `message`, `TextScope` adds `text`,
`CommandScope` adds `commandArgs`, and callback scopes add `callbackQuery`.

## Commands and callbacks

```kotlin
dispatchers {
    command("/start") {
        bot.sendMessage(
            chatId,
            "Choose an action",
            replyMarkup = buildInlineKeyboardMarkup {
                callback("Ping", "menu:ping")
                addRow {
                    url("Telegram", "https://telegram.org")
                    copyText("Copy", "copied text")
                }
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
    }

    document {
        bot.sendMessage(chatId, "Document: " + document.fileName)
    }
}
```

Command matching accepts `/cmd` or `/cmd args`, not `/cmdsuffix`.
`commandArgs` is `null` when there are no arguments.

The overload that takes `description` and `addToBotCommands` is deprecated.
Configure Telegram's visible command menu explicitly:

```kotlin
init {
    setMyCommands(
        listOf(
            BotCommand("/start", "Start the bot"),
            BotCommand("/help", "Show help")
        )
    ).consume()
}
```

## Raw handlers

Use raw handlers only when the DSL does not express the check. Always narrow
`allowedKinds` when possible.

```kotlin
dispatchers {
    addHandler(setOf(MessageUpdate)) { update, bot, _ ->
        val message = (update as MessageUpdate).message
        val value = message.text ?: return@addHandler Decision.Next
        if (!value.startsWith("/raw ")) return@addHandler Decision.Next

        bot.sendMessage(
            message.chat.id.toChatId(),
            value.removePrefix("/raw ")
        )
        Decision.Consumed
    }
}
```

## Filters and control helpers

Inspect `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/` for exact
signatures. Common tools include:

- `filters { ... }` and `addHandlerFirst(...)`
- `messageRateFilter(...)`
- `testEach { ... }`
- `scopeOfType(...) { ... }`
- `contextHook { ... }` and `debugHook { ... }`
- `replayUpdates(...)`
- `consumeUpdatesInChannel(...)`
- `handleUnknownUpdate { ... }`

Ordering matters: filters inserted at the front run before normal handlers, and
a consuming handler prevents later handlers unless a wrapper changes that
behavior.

## Builders

Read builder sources for the exact options; do not reconstruct generated model
constructors from this guide.

Inline keyboard rows:

```kotlin
val markup = buildInlineKeyboardMarkup {
    callback("Save", "save")
    addRow {
        url("Docs", "https://core.telegram.org/bots/api")
        copyText("Copy", "payload")
    }
}
```

Message entities:

```kotlin
val content = buildEntityString {
    addString("Important", Bold + Italic)
    addString(": read this")
}

bot.sendMessage(chatId, content)
```

For rich messages, inspect
`kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/KittyBot.kt` for request
methods,
`kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/classes/rich/` for
models, and
`samples/src/main/kotlin/RichTextExample.kt` for working usage.
