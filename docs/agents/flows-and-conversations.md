# Flows and Conversations

Read this file for state handlers, section flows, routing, high-level and
lightweight conversations, media groups, and command groups.

## Source pointers

- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/flow/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/route/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/conv/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/conv2/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/mgroup/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/comms/`
- `samples/src/main/kotlin/`

## State handlers

Use `stateHandler` when application state selects a branch of handlers.

```kotlin
sealed interface ProfileState {
    data object WaitingName : ProfileState
    data class Active(val name: String) : ProfileState
}

val states = ConcurrentHashMap<Long, ProfileState>()

dispatchers {
    stateHandler(
        stateFinder = { update, _ ->
            val message = (update as? MessageUpdate)?.message
                ?: return@stateHandler null
            states[message.chat.id]
        },
        allowedKinds = setOf(MessageUpdate)
    ) {
        valueOf(ProfileState.WaitingName) {
            text {
                states[message.chat.id] =
                    ProfileState.Active(message.text ?: "Unknown")
                bot.sendMessage(chatId, "Saved")
            }
        }

        instanceOf<ProfileState.Active> {
            text("/profile") {
                bot.sendMessage(chatId, "Profile is active")
            }
        }

        default {
            text {
                bot.sendMessage(chatId, "No matching state")
            }
        }
    }
}
```

Branches are checked in declaration order. `valueOf` uses equality,
`classOf<T>` requires the exact class, and `instanceOf<T>` accepts subclasses.
A `null` state skips the state handler.

## Flow handlers

`flowHandler` routes an identity through numbered or named sections. Use it when
the flow position should be explicit state rather than a suspended coroutine.

Section helpers on `HandlerContext` include:

- `nextSection(args)`
- `nextSectionWithName(name, args)`
- `nextSectionWithId(id, args)`
- `pauseSection(args)`
- `resetFlow()`
- `getFlowArgs()`
- `getFlowIdentityData()`

The default flow storage is in memory. Supply a `FlowIdentityStorage` when state
must survive restarts or be shared between processes. See
`samples/src/main/kotlin/FlowExample.kt` before creating a new flow.

## High-level conversations

`bot.conv.buildConversation` provides entry-point DSLs and typed
`receive*().await()` helpers. Use it for the common one-conversation-per-chat
model.

```kotlin
fun FelineDispatcher.signupConversation() {
    buildConversation {
        onText("/signup") {
            bot.sendMessage(chatId, "What is your name?")
            val name = receiveText().await()

            bot.sendMessage(chatId, "Send a profile photo, " + name)
            val photos = receivePhotos().await()
            bot.sendMessage(chatId, "Received " + photos.size + " sizes")
        }
    }
}
```

Entry points include `onStartCommand`, `onText`, and `onMsg`. Typed waiters are
under
`kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/conv/cts/`.
`endConversation()` cancels the conversation.

## Lightweight conversations

`bot.conv2` adds one `ConversationManager` handler and leaves job uniqueness,
cancellation, and matching policy to the application.

```kotlin
fun HandlerStore.manualConversationExample() {
    val manager = conversations(setOf(MessageUpdate))

    command("/ask") {
        val api = bot
        val targetChat = chatId
        val targetChatId = message.chat.id

        manager.newConversation {
            api.sendMessage(targetChat, "What should I remember?")
            val answer = receiveText(targetChatId).await()
            api.sendMessage(targetChat, "Remembered: " + answer)
        }
    }
}
```

`receiveText(chatId, filter)` is the supplied convenience waiter. For other
updates, provide an `UpdateReceiver<T>` or use `receive(kind) { ... }`.

Multiple jobs can wait simultaneously. The first matching queued receiver that
completes consumes the update, so explicitly track and cancel jobs when only one
conversation should exist for an identity.

## Routing

Use `routingInt` or `routing<T>` when a key should jump directly to one handler
section. This avoids testing unrelated section handlers.

- `routingInt` uses `Int.MIN_VALUE` for no key.
- `routing<T>` uses `null` for no key.
- `section(key)` defines a destination.
- `default` handles unknown keys.
- `common(handler)` runs before the selected section.

Use the current routing functions without the deprecated `updateKinds`
constructor overloads. See `samples/src/main/kotlin/RoutingExample.kt`.

`callbackQueryRoute` is a smaller map specialized for callback-query data.

## Media groups

`setupMediaGroupHandler(...)` plus the `mediaGroup` DSL transforms sequential
message updates into a grouped update. It requires update ordering by chat.

`mediaGroupHandler(...)` is the direct accumulator style. It groups by chat and
media-group id, then invokes the block after an inactivity window.

With `UpdaterMode.MultiThread`, choose a chat-based `MultiIdentity` for either
style.

## Command groups

`commandGroupHandler` maps commands to annotated methods returning `ExecBlock`.
Supported arguments include `String`, `Long`, `Double`, and lists of supported
types. `twoStepCommandGroupHandler` accepts arguments from a later message.

See `samples/src/main/kotlin/CommandGroupExample.kt` before adding a command
group; reflection and parsing behavior live under
`kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/action/comms/`.
