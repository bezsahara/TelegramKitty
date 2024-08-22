package org.bezsahara.kittybot.telegram.utils

import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.telegram.client.TelegramError


/**
 * A sealed interface representing the result of an operation.
 * @param T The type of the result in case of success.
 */
sealed interface TResult<out T> {

    /**
     * A sealed interface representing a result that can be one of two types.
     *
     * @param First The type of the first possible result.
     * @param Second The type of the second possible result.
     */
    sealed interface Either<out First, out Second>

    /**
     * A class representing a successful result.
     *
     * @param C The type of the value contained in a successful result.
     * @property value The value of the successful result.
     */
    class Success<out C>(@JvmField val value: C) : TResult<C> {

        /**
         * A class representing the first type in an `Either` result.
         *
         * @param First The type of the first possible result.
         * @property value The value of the first type.
         */
        class First<out First>(@JvmField val value: First) : Either<First, Nothing>

        /**
         * A class representing the second type in an `Either` result.
         *
         * @param Second The type of the second possible result.
         * @property value The value of the second type.
         */
        class Second<out Second>(@JvmField val value: Second) : Either<Nothing, Second>
    }

    /**
     * A class representing a failed result.
     *
     * @property cause A `TelegramError` instance describing the error.
     */
    class Failure(@JvmField val cause: TelegramError) : TResult<Nothing>, Either<Nothing, Nothing> {
        override fun toString(): String {
            return "ErrorCode: ${cause.errorCode}, problem: ${cause.description}"
        }
    }
}

/**
 * @throws KittyError
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> TResult<T>.unwrap(): T {
    return when (this) {
        is TResult.Failure -> throw KittyError(toString())
        is TResult.Success -> this.value
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T> TResult<T>.unwrapOrNull(): T? {
    return when (this) {
        is TResult.Failure -> null
        is TResult.Success -> this.value
    }
}

inline fun <T, R> TResult<T>.onResult(onFailure: (TResult.Failure) -> R, onSuccess: (T) -> R): R = when (this) {
    is TResult.Failure -> onFailure(this)
    is TResult.Success -> onSuccess(this.value)
}

inline fun <T, R> TResult<T>.onSuccess(onSuccess: (T) -> R): R = when (this) {
    is TResult.Failure -> throw KittyError(cause.toString())
    is TResult.Success -> onSuccess(this.value)
}

inline fun <T> TResult<T>.onFailure(onFailure: (TResult.Failure) -> Unit): TResult<T>? = when (this) {
    is TResult.Failure -> {
        onFailure(this); null
    }

    is TResult.Success -> this
}

inline fun <F, S, R> TResult.Either<F, S>.onResult(onFailure: (TResult.Failure) -> R, onSuccess: (F?, S?) -> R): R {
    return when (this) {
        is TResult.Failure -> onFailure(this)
        is TResult.Success.First -> onSuccess(this.value, null)
        is TResult.Success.Second -> onSuccess(null, this.value)
    }
}

inline fun <F, S, R> TResult.Either<F, S>.onResult(
    onFailure: (TResult.Failure) -> R,
    onFirstSuccess: (F) -> R,
    onSecondSuccess: (S) -> R
): R = when (this) {
    is TResult.Failure -> onFailure(this)
    is TResult.Success.First -> onFirstSuccess(this.value)
    is TResult.Success.Second -> onSecondSuccess(this.value)
}

/**
 * Can be used to take only one type from a return of a method that can return two types.
 * If another type is returned, this does nothing.
 * @see TResult.Either
 */
inline fun <reified T> TResult.Either<*, *>.onSpecificSuccessOf(onSuccess: (T) -> Unit) {
    when (this) {
        is TResult.Failure -> Unit
        is TResult.Success.First -> if (this.value is T) {
            onSuccess(this.value)
        }

        is TResult.Success.Second -> if (this.value is T) {
            onSuccess(this.value)
        }
    }
}

inline fun <reified T> TResult.Either<*, *>.onSpecificResult(
    onFailure: (TResult.Failure) -> Unit,
    onSuccess: (T) -> Unit
) {
    when (this) {
        is TResult.Failure -> onFailure(this)
        is TResult.Success.First -> if (this.value is T) {
            onSuccess(this.value)
        }

        is TResult.Success.Second -> if (this.value is T) {
            onSuccess(this.value)
        }
    }
}

inline fun <F, S> TResult.Either<F, S>.onResult(builder: EitherResultResponse<F, S>.() -> Unit) =
    EitherResultResponse<F, S>().apply(builder).execute(this)

class EitherResultResponse<out F, out S> {
    private var f1: ((@UnsafeVariance F) -> Unit)? = null
    private var s1: ((@UnsafeVariance S) -> Unit)? = null
    private var e1: ((TResult.Failure) -> Unit)? = null

    fun onFirstSuccess(block: (F) -> Unit) {
        f1 = block
    }

    fun onSecondSuccess(block: (S) -> Unit) {
        s1 = block
    }

    fun onFailure(block: (TResult.Failure) -> Unit) {
        e1 = block
    }

    fun execute(value: TResult.Either<@UnsafeVariance F, @UnsafeVariance S>) {
        when (value) {
            is TResult.Failure -> e1?.invoke(value)
            is TResult.Success.First -> f1?.invoke(value.value)
            is TResult.Success.Second -> s1?.invoke(value.value)
        }
    }
}



