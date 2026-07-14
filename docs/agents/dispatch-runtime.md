# Dispatch Runtime

Read this file for handlers, decisions, update kinds, identities, per-update
context, errors, and concurrency.

## Source pointers

- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/dispatchers/Handler.kt`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/dispatchers/FelineDispatcher.kt`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/HandlerContext.kt`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/FurballConfig.kt`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/furballs/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/bot/updates/updaters/`
- `kittybot/src/main/kotlin/org/bezsahara/kittybot/telegram/classes/core/update/`

## Handler contract

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

Handlers run in registration order, filtered by `allowedKinds`:

- `null` accepts every update kind.
- An empty set accepts no update directly; this is useful for jump points.
- A specific set lets the dispatcher skip unrelated handlers.

The dispatcher builds jump tables by `UpdKind`. Prefer
`updKindSetOf(MessageUpdate, CallbackQueryUpdate)` or a normal
`Set<UpdKind>`. `UpdKindSet` and `UpdKindMutSet` are compact bit-backed set
implementations; use the mutable form only when mutation is actually needed.

Each generated `Update` subtype has a companion object that is its `UpdKind`.
For example, `MessageUpdate` is both a type and the value used in
`setOf(MessageUpdate)`.

## Decisions

- `Decision.Next` continues with the next matching handler.
- `Decision.Consumed` ends processing for the update.
- `Decision.NextTo(identity)` jumps to a handler.
- `Decision.AfterNextTo(identity, adjust)` jumps after a handler.

Jump targets require a stable `HandlerIdentity`. Store the identity in a `val`,
or use a library handler/delegate that owns one. Never return
`HandlerIdentity.createNew()` from a getter.

Duplicate identities are rejected while the dispatcher is prepared unless
`furballConfig.ignoreIdentityDuplicated` is enabled.

## HandlerContext and botContext

`HandlerContext` is created once per update and shared through that update's
handler chain. Use it for derived or temporary values, not persistent bot state.

Create `AttrKey` values during dispatcher setup:

```kotlin
dispatchers {
    val normalizedText = attrKeyOf<String>("normalized-text")

    contextHook(setOf(MessageUpdate)) { update, context ->
        context[normalizedText] =
            (update as MessageUpdate).message.text.orEmpty().trim().lowercase()
    }

    text {
        val normalized = handlerContext[normalizedText]
        // use normalized
    }
}
```

`AttrKey` values belong to one bot's `IdentityScope`. Do not create them after
startup or reuse them across bots.

`botContext` is a bot-lifetime `TypeAwareMap` intended for integrations and
framework state. Application state can use its own appropriately synchronized
store.

## Errors

A `HandlerErrorHandler` receives the exception, API client, update,
`HandlerContext`, and failed handler, then returns a `Decision`.

```kotlin
setErrorHandler { throwable, bot, update, _, _ ->
    update.chatIdOrNull()?.let { chatId ->
        bot.sendMessage(chatId, "Handler failed")
    }
    Decision.Consumed
}
```

The default prints the stack trace and consumes the update. Configure an
explicit production policy.

## Concurrency

`UpdaterMode.MultiThread` preserves order only for updates that resolve to the
same non-null `MultiIdentity`. Different identities and `null` identities can
run concurrently.

This matters for:

- mutable maps and caches
- media-group accumulation
- state machines and conversations
- rate limits
- file or database transactions

Use thread-safe state and choose the identity expected by the feature.

## Runtime configuration

Most bots should keep `FurballConfig.Default`.

Important fields:

- `hopSafetyTimes` limits repeated jump dispatch.
- `attrsLimit` bounds registered context keys.
- `onRecursionProblem` overrides the default recursion failure.
- `multiUpdaterUseMap` and `multiUpdaterMapLimit` configure the multi-updater
  shard store.
- `httpTimeout` derives the HTTP timeout from the polling timeout.
- `useFurballContVariant` selects the alternative continuation-based dispatcher
  implementation. Keep the default unless deliberately testing that runtime.

## Dynamic handlers and visitor mode

`createDynamicHandlerStore()` creates a registry whose add/remove operations
publish immutable reader snapshots. Use `collectAndAdd { ... }` when a block's
handlers must later be removed together.

`useUpdatesVisitor(UpdateVisitor)` replaces the handler list with typed visitor
callbacks. It cannot be combined with `dispatchers { ... }`.
