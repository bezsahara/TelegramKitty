package org.bezsahara.kittybot.other;

import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function2;
import org.bezsahara.kittybot.bot.KittyBot;
import org.bezsahara.kittybot.bot.dispatchers.Decision;
import org.bezsahara.kittybot.bot.dispatchers.Handler;
import org.bezsahara.kittybot.bot.updates.HandlerContext;
import org.bezsahara.kittybot.telegram.classes.core.update.Update;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class HandlerNS<S> implements Handler {
    private final Function2<S, Continuation, Object> invoker;

    public HandlerNS(Function2<S, Continuation, Object> invoker) {
        this.invoker = Objects.requireNonNull(invoker);
    }

    public abstract @Nullable S allowUpdate(@NotNull Update update, @NotNull KittyBot bot, @NotNull HandlerContext handlerContext);

    private static class State implements Continuation {
        private final Continuation parent;

        public State(@NotNull Continuation parent) {
            this.parent = parent;
        }

        @Override
        public void resumeWith(@NotNull Object o) {
            // since the call is technically the last one.
            if (o instanceof Result.Failure) {
                parent.resumeWith(o);
                return;
            }
            parent.resumeWith(Decision.Consumed);
        }

        @Override
        public @NotNull CoroutineContext getContext() {
            return parent.getContext();
        }
    }

    @Override
    public final @NotNull Object handleUpdate(@NotNull Update update,
                                              @NotNull KittyBot bot,
                                              @NotNull HandlerContext handlerContext,
                                              @NotNull Continuation<? super @NotNull Decision> $completion) {
        var scope = allowUpdate(update, bot, handlerContext);
        if (scope == null) return Decision.Next;
        var res = invoker.invoke(scope, new State($completion));
        if (res == CoroutineSingletons.COROUTINE_SUSPENDED) {
            return CoroutineSingletons.COROUTINE_SUSPENDED;
        }
        return Decision.Consumed;
    }
}
