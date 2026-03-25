@file:Suppress("NOTHING_TO_INLINE", "IfThenToSafeAccess")

package org.bezsahara.kittybot.telegram.utils

import org.bezsahara.kittybot.telegram.client.TelegramError
import org.bezsahara.kittybot.telegram.client.TelegramErrorException

sealed interface TReturns

@JvmInline
value class TResult<out T>(@PublishedApi internal val value: T) : TReturns {
    @JvmInline
    value class Either<out First, out Second>(val value: Any) : TReturns {
        val isError: Boolean
            get() = value is TelegramError

        val isSuccess: Boolean
            get() = value !is TelegramError

        inline fun onError(block: (TelegramError) -> Unit) {
            if (value is TelegramError) {
                block(value)
            }
        }

        fun consume() {
            if (value is TelegramError)  {
                throw TelegramErrorException(value)
            }
        }

        companion object {
            fun <T: Any> First(o: T): Either<T, Nothing> {
                return Either(o)
            }

            fun <T: Any> Second(o: T): Either<Nothing, T> {
                return Either(o)
            }
        }
    }

    val isError: Boolean
        get() = value is TelegramError

    val isSuccess: Boolean
        get() = value !is TelegramError

    inline fun onSuccess(block: (T) -> Unit) {
        if (value !is TelegramError) {
            block(value)
        }
    }

    inline fun onError(block: (TelegramError) -> Unit) {
        if (value is TelegramError) {
            block(value)
        }
    }

    fun consume() {
        if (value is TelegramError)  {
            throw TelegramErrorException(value)
        }
    }
}

fun <T> TResultFailure(e: TelegramError): TResult<T> {
    return TResult(e) as TResult<T>
}

fun TResultFailureEither(e: TelegramError): TResult.Either<Nothing, Nothing> {
    return TResult.Either(e)
}

inline fun <reified First> TResult.Either<First, *>.onFirstSuccess(block: (First) -> Unit) {
    if (value is First) {
        block(value)
    }
}

inline fun <reified Second> TResult.Either<*, Second>.onSecondSuccess(block: (Second) -> Unit) {
    if (value is Second) {
        block(value)
    }
}

inline fun <T> TResult<T>.unwrap(): T {
    if (value !is TelegramError) {
        return value
    } else {
        throw TelegramErrorException(value)
    }
}


inline fun <reified First> TResult.Either<First, *>.unwrapFirst(): First {
    if (value is First) {
        return value
    } else {
        throw generateErrorMsgForEither(value, false)
    }
}

inline fun <reified Second> TResult.Either<*, Second>.unwrapSecond(): Second {
    if (value is Second) {
        return value
    } else {
        throw generateErrorMsgForEither(value, true)
    }
}

inline fun <reified T> TResult.Either<*, *>.expect(): T {
    if (value is T) {
        return value
    } else {
        throw IllegalStateException("Function returned unexpected result!")
    }
}

inline fun <T> TResult<T>.unwrapOrNull(): T? {
    return if (value !is TelegramError) {
        value
    } else {
        null
    }
}

inline fun <reified First> TResult.Either<First, *>.unwrapFirstOrNull(): First? {
    return if (value is First) {
        value
    } else {
        null
    }
}

inline fun <reified Second> TResult.Either<*, Second>.unwrapSecondOrNull(): Second? {
    return if (value is Second) {
        value
    } else {
        null
    }
}

inline fun <T, R> TResult<T>.onResult(
    onError: (error: TelegramError) -> R,
    onSuccess: (T) -> R
): R {
    return if (isSuccess) {
        onSuccess(value)
    } else {
        onError(value as TelegramError)
    }
}

fun <T> TelegramError.asTResult(): TResult<T> {
    return TResultFailure(this)
}

@PublishedApi
internal fun generateErrorMsgForEither(obj: Any, side: Boolean): Throwable {
    if (obj is TelegramError) {
        return TelegramErrorException(obj)
    }
    return if (side) IllegalStateException("Function returned left value but right was expected!")
    else IllegalStateException("Function returned right value but left was expected!")
}