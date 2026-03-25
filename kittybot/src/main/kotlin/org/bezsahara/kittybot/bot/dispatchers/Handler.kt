package org.bezsahara.kittybot.bot.dispatchers

import org.bezsahara.kittybot.bot.IdentityScope
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import java.util.concurrent.atomic.AtomicInteger

/**
 * Key for values stored inside [org.bezsahara.kittybot.bot.updates.HandlerContext].
 *
 * Keys are tied to a specific [IdentityScope]. Create them before the bot starts and do not
 * mix keys between different bot instances.
 *
 * You can create it in [FelineDispatcher] by using its [IdentityScope].
 *
 * Example:
 * ```kotlin
 * val lastMessageId = FelineDispatcher.identityScope.attrKeyOf<Long>("lastMessageId")
 * ```
 */
open class AttrKey<T> internal constructor(val name: String?, val clazz: Class<T>?, id: Int, val scope: IdentityScope) {
    constructor(name: String?, clazz: Class<T>?, identityScope: IdentityScope) :
            this(name, clazz, identityScope.newAttrKeyId(), identityScope)
    @JvmField
    val id = id

    override fun toString(): String {
        return "AttrKey<${clazz?.name ?: ""}>[id = $id](${name ?: ""})"
    }
}

/**
 * Stable handler id used by [Decision.NextTo] and related jump operations.
 */
@JvmInline
value class HandlerIdentity internal constructor(val value: Int) {
    fun toDecisionNextTo(): Decision {
        return Decision.NextTo(this)
    }

    companion object {
        internal val emptyID = HandlerIdentity(Int.MIN_VALUE)
        private val generator = AtomicInteger(0)

        fun createNew(): HandlerIdentity {
            return HandlerIdentity(generator.getAndAdd(1))
        }

        fun of(id: Int): HandlerIdentity {
            if (id < 0) throw IllegalArgumentException("Id must be greater than or equal to 0")
            return HandlerIdentity(id)
        }
    }
}

/**
 * Convenience base class for handlers that have a fixed set of accepted update kinds and
 * sometimes need a real [identity] field.
 */
abstract class SimpleHandler(vararg updateKinds: UpdateKind<*>) : Handler {
    final override val identity: HandlerIdentity by HandlerIdentityDelegate()

    final override val allowedKinds: Set<UpdateKind<*>>? = updateKinds.let { if (it.isEmpty()) null else it.toSet() }
}


/**
 * Core handler contract.
 *
 * A handler receives an [Update] and returns a [Decision] that controls what the dispatcher
 * should do next.
 *
 * Rules:
 *
 * - [identity] must be backed by a stable field, not a custom getter. Returning
 *   `HandlerIdentity.createNew()` from a getter will generate a different id on each access and
 *   will break jump-based dispatch.
 * - [allowedKinds] should be `null` to accept any update kind. Returning an empty set means the
 *   handler accepts nothing.
 * - If you store values in [org.bezsahara.kittybot.bot.updates.HandlerContext], define the
 *   needed [AttrKey]s before startup and only use keys from the same bot's [IdentityScope].
 *
 * Lambda-created handlers are fine for simple cases. If you need to override [identity] or
 * [allowedKinds] on such handlers, wrap them with the helpers from `HandlerDelegate.kt`.
 *
 * @see HandlerDelegate
 */
fun interface Handler {
    val identity: HandlerIdentity? get() = null

    val allowedKinds: Set<UpdateKind<*>>? get() = null

    suspend fun handleUpdate(update: Update, bot: KittyBot, handlerContext: HandlerContext): Decision
}

/**
 * Result of handler execution.
 *
 * Use [Consumed] to stop processing, [Next] to continue with the next matching handler, or
 * [NextTo]/[AfterNextTo] to jump to a handler by [HandlerIdentity].
 *
 * This is kept as an int-backed class instead of a sealed hierarchy to keep the hot dispatch
 * path cheap.
 */
class Decision
internal constructor(@JvmField val result: Int, @JvmField val offset: Int) {
    fun isNext(): Boolean { return result == NEXT }
    fun isConsumed(): Boolean { return result == CONSUMED }

    @Suppress("FunctionName")
    companion object {
        private val cachedMappings = Array(101) { Decision(it, 0) }
        private val cachedMappingsOffset1 = Array(101) { Decision(it, 1) }

        // Return to jump to a handler with specified HandlerIdentity
        fun NextTo(handlerIdentity: HandlerIdentity): Decision {
            return if (handlerIdentity.value > 100) Decision(
                handlerIdentity.value,
                0
            ) else cachedMappings[handlerIdentity.value]
        }

        // Return to jump after the handler with specified HandlerIdentity
        fun AfterNextTo(handlerIdentity: HandlerIdentity): Decision {
            return if (handlerIdentity.value > 100) Decision(
                handlerIdentity.value,
                1
            ) else cachedMappingsOffset1[handlerIdentity.value]
        }

        // Return to jump to a Handler with offset with specified HandlerIdentity
        fun NextTo(handlerIdentity: HandlerIdentity, offset: Int): Decision {
            return if (handlerIdentity.value > 100 || offset != 1) Decision(
                handlerIdentity.value,
                offset
            ) else cachedMappingsOffset1[handlerIdentity.value]
        }

        // Return to consume the update and stop its propagation to other handlers if any
        @JvmField
        val Consumed = Decision(CONSUMED, 0)

        // Return to suggest to test the next handler if any
        @JvmField
        val Next = Decision(NEXT, 0)

        const val CONSUMED = -2
        const val NEXT = -1
    }

    override fun equals(other: Any?): Boolean = other is Decision && other.result == result && other.offset == offset

    override fun hashCode(): Int = 31 * result + offset

    override fun toString(): String {
        return when (result) {
            CONSUMED -> "Decision.Consumed"
            NEXT -> "Decision.Next"
            else -> "Decision($result, $offset)"
        }
    }
}
