@file:Suppress("NOTHING_TO_INLINE", "IfThenToSafeAccess")

package org.bezsahara.kittybot.telegram.utils

import org.bezsahara.kittybot.telegram.client.TelegramError
import org.bezsahara.kittybot.telegram.client.TelegramErrorException

sealed interface TReturns {
    val value: Any?
}

// value class here will be worse due to how kotlin coroutines code gen works
data class TResult<out T>(override val value: T) : TReturns {
    data class Either<out First, out Second>(override val value: Any) : TReturns {
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
            if (value is TelegramError) {
                throw TelegramErrorException(value)
            }
        }

        fun errorOrNull(): TelegramError? {
            return if (value is TelegramError) value else null
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

    inline fun onErrorTerminate(block: (TelegramError) -> Nothing): T {
        if (value is TelegramError) {
            block(value)
        } else {
            return value
        }
    }

    fun consume() {
        if (value is TelegramError) {
            throw TelegramErrorException(value)
        }
    }

    fun errorOrNull(): TelegramError? {
        return if (value is TelegramError) value else null
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
    consume()
    return value
}

fun TResult<Boolean>.asBoolean(): Boolean {
    return isSuccess
}

inline fun <reified First> TResult.Either<First, *>.unwrapFirst(): First {
    if (value is First) {
        return value
    } else {
        generateErrorMsgForEither(value, false)
    }
}

inline fun <reified Second> TResult.Either<*, Second>.unwrapSecond(): Second {
    if (value is Second) {
        return value
    } else {
        generateErrorMsgForEither(value, true)
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
    onSuccess: (T) -> R,
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

internal fun TResult<*>.throwError(): Nothing {
    val a = (value as? TelegramError) ?: error("value is not a TelegramError")
    throw TelegramErrorException(a)
}

fun TReturns.errorOrNull(): TelegramError? {
    val v = value
    if (v is TelegramError) {
        return v
    }
    return null
}

@PublishedApi
internal fun generateErrorMsgForEither(obj: Any, side: Boolean): Nothing {
    if (obj is TelegramError) {
        throw TelegramErrorException(obj)
    }
    throw IllegalStateException(if (side) "Function returned left value but right was expected!" else "Function returned right value but left was expected!")
}

val TResultTrue = TResult(true)