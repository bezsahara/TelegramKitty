package org.bezsahara.kittybot.other;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlinx.coroutines.channels.Channel;
import org.bezsahara.kittybot.bot.KittyBot;
import org.bezsahara.kittybot.bot.KittyBotResult;
import org.bezsahara.kittybot.bot.dispatchers.Decision;
import org.bezsahara.kittybot.bot.updates.HandlerContext;
import org.bezsahara.kittybot.bot.updates.HandlerContextArray;
import org.bezsahara.kittybot.bot.updates.HandlerException;
import org.bezsahara.kittybot.bot.updates.furballs.FurballDispatchersCont;
import org.bezsahara.kittybot.telegram.classes.core.update.Update;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CancellationException;

public class FDC extends FurballDispatchersCont {
    public FDC(@NotNull KittyBot bot, @NotNull Channel<@NotNull Update> channel, @NotNull KittyBotResult result) {
        super(bot, channel, result);
    }

    private final class FDCState implements Continuation<Object> {
        public final Continuation parent;
        public final HandlerContext handlerContext;
        public final int[] jumpTable;
        public final Update update;
        public int hopSafety;
        public int state;
        public Object res;
        public int pos;

        public FDCState(
                Continuation parent,
                int pos,
                int[] jumpTable,
                Update update
        ) {
            this.parent = parent;
            this.pos = pos;
            this.jumpTable = jumpTable;
            this.update = update;
            hopSafety = 0;
            state = 0;
            res = null;
            handlerContext = new HandlerContextArray(attrKeyMaxSize, identityScope, channel);
        }

        @Override
        public void resumeWith(@NotNull Object o) {
            res = o;
            Object out;
            try {
                out = applyHandlers(update, this);
            } catch (Throwable throwable) {
                parent.resumeWith(new Result.Failure(throwable));
                return;
            }
            if (out != CoroutineSingletons.COROUTINE_SUSPENDED) {
                parent.resumeWith(Unit.INSTANCE);
            }
        }

        @Override
        public @NotNull CoroutineContext getContext() {
            return parent.getContext();
        }
    }

    final static CoroutineSingletons CS = CoroutineSingletons.COROUTINE_SUSPENDED;

    @Override
    final public @Nullable Object applyHandlers(@NotNull Update update, @NotNull Continuation<? super @NotNull Unit> $completion) throws Throwable {
        int pos;
        int[] jumpTable;
        int hopSafety;
        FDCState fdState;

        if (!($completion instanceof FDCState)) {
            jumpTable = handlerByKindMap[update.getOrdinal()];
            if (jumpTable == null) {
                return Unit.INSTANCE;
            }
            pos = jumpTable[hlSize];
            if (hlSize <= pos) return Unit.INSTANCE;
            hopSafety = 0;
            fdState = new FDCState($completion, pos, jumpTable, update);
        } else  {
            fdState = ((FDCState) $completion);
            pos = fdState.pos;
            jumpTable = fdState.jumpTable;
            hopSafety = fdState.hopSafety;
        }
        var handlerContext = fdState.handlerContext;

        Decision res = null;

        switch (fdState.state) {
            case 0 -> {
                var handler = handlerList[pos];
                Object r0;
                try {
                    fdState.state = 1;
                    r0 = handler.handleUpdate(update, bot, handlerContext, fdState);
                    if (r0 == CS) {
                        return CS;
                    }
                } catch (CancellationException e) {
                    throw e;
                } catch (Throwable throwable) {
                    fdState.state = 2;
                    r0 = errorHandler.handleException(throwable, bot, update, handlerContext, handler, fdState);
                    if (r0 == CS) {
                        return CS;
                    }
                }
                res = (Decision) r0;
            }
            case 1 -> {
                var handler = handlerList[pos];
                var value = fdState.res;
                if (value instanceof Result.Failure) {
                    fdState.state = 2;
                    var r0 = errorHandler.handleException((((Result.Failure) value).exception), bot, update, handlerContext, handler, fdState);
                    if (r0 == CS) {
                        return CS;
                    }
                    res = (Decision) r0;
                } else  {
                    res = (Decision) value;
                }
                fdState.state = 0;
            }
            case 2 -> {
                var stored = fdState.res;
                if (stored instanceof Result.Failure) {
                    throw (((Result.Failure) stored).exception);
                }
                res = (Decision) stored;
                fdState.state = 0;
            }
        }

        outer:
        while (true) {
            if (hopSafety > hopSafetyLimit) {
                if (furballConfig.getOnRecursionProblem() != null) {
                    furballConfig.getOnRecursionProblem().invoke(hopSafety);
                } else {
                    throw new HandlerException("It seems there is a recursion problem!");
                }
            }

            if (res == null) {
                throw new NullPointerException("Decision is null!");
            }

            switch (res.result) {
                // CONSUMED
                case -2 -> { break outer; }
                // NEXT
                case -1 -> {
                    pos = jumpTable[pos];
                    if (hlSize <= pos) break outer;
                }
                default -> {
                    hopSafety += 1;
                    pos = handlerListIdentity.get(res.result);
                    if (pos == -1) {
                        pos = maybeDynHI(res, handlerContext, update.getOrdinal());
                        if (pos == -1) {
                            throw new HandlerException("Did not find a handler " + res.result + "!");
                        }
                    } else  {
                        int offset = res.offset;
                        if (offset != 0) {
                            pos += offset;
                            if (pos < 0) {
                                throw new HandlerException("pos is less than 0 after applying offset of " + offset + "!");
                            }
                            if (hlSize <= pos) break outer;
                        }
                        if (res.adjust) {
                            pos = pos == 0 ? jumpTable[hlSize] : jumpTable[pos - 1];
                            if (hlSize <= pos) break outer;
                        }
                    }
                }
            }

            var handler = handlerList[pos];
            try {
                fdState.pos = pos;
                fdState.hopSafety = hopSafety;
                fdState.state = 1;
                var r0 = handler.handleUpdate(update, bot, handlerContext, fdState);
                if (r0 == CS) {
                    return CS;
                }
                res = (Decision) r0;
            } catch (CancellationException e) {
                throw e;
            } catch (Throwable throwable) {
                fdState.state = 2;
                var r0 = errorHandler.handleException(throwable, bot, update, handlerContext, handler, fdState);
                if (r0 == CS) {
                    return CS;
                }
                res = (Decision) r0;
            }
        }

        return Unit.INSTANCE;
    }
}
