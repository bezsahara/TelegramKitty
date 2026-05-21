package org.bezsahara.kittybot.bot.action.flow

import org.bezsahara.kittybot.bot.action.route.KeyGeneratorInt
import org.bezsahara.kittybot.bot.action.route.RoutingStrategyInt
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.updates.HandlerContext
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import java.lang.reflect.Modifier

/**
 * Registers state-based routing for the current handler store.
 *
 * Branches are checked in declaration order and the first matching branch is used.
 * Return `null` from [stateFinder] when no state branch should be selected.
 *
 * Example:
 * ```kotlin
 * stateHandler(::findState) {
 *     valueOf(MyState.WaitingName) { ... }
 *     instanceOf<MyState.Active> { ... }
 * }
 * ```
 */
inline fun <T> TransparentHandlerStore.stateHandler(
    stateFinder: StateFinder<T>,
    allowedKinds: Set<UpdKind>? = null,
    builder: StateHandlerBuilder<T>.() -> Unit,
) {
    val builderHandler = StateHandlerBuilder<T>(stateFinder, this, allowedKinds)
    builder.invoke(builderHandler)
    builderHandler.build()
}


/**
 * Resolves the current state for an update.
 *
 * Return `null` when this update should not enter any state branch.
 */
fun interface StateFinder<T> {
    fun find(update: Update, context: HandlerContext): T?
}

/**
 * DSL builder used by [stateHandler].
 *
 * Add branches with [valueOf], [classOf], or [instanceOf].
 */
class StateHandlerBuilder<T>(
    private val stateFinder: StateFinder<T>,
    handlerStore: TransparentHandlerStore,
    allowedKinds: Set<UpdKind>? = null,
) {
    private val map = linkedMapOf<StateTester, Int>()
    fun addToMap(stateTester: StateTester, state: Int) {
        require(map.put(stateTester, state) == null) { "Map already has state $state" }
    }

    val contextKey = handlerStore.attrKeyOf<StateHolder<T>>("StateHolder")
    @PublishedApi
    internal fun createStateGen(canDefault: Boolean) = StateGeneratorInt(stateFinder, contextKey, map, canDefault)
    @PublishedApi
    internal val routing = RoutingStrategyInt(createStateGen(false), handlerStore, allowedKinds)
    private var index = 0

    /**
     * Matches when the resolved state has exactly the runtime class [A].
     *
     * This is an exact class match, not an `is` check.
     */
    inline fun <reified A : T> classOf(block: StateHandlerScope<A>.() -> Unit) = classOf(A::class.java, block)

    /**
     * Matches when the resolved state has exactly the runtime class [clazz].
     *
     * This is an exact class match, not an `is` check.
     */
    inline fun <A : T> classOf(clazz: Class<out A>, block: StateHandlerScope<A>.() -> Unit) {
        val key = getIndex()
        addToMap(StateTester.ByClass(clazz), key)
        routing.section(key) {
            val shs = StateHandlerScope(this, contextKey as AttrKey<StateHolder<A>>)
            shs.block()
        }
    }

    /**
     * Matches when the resolved state is an instance of [A].
     */
    inline fun <reified A : T> instanceOf(block: StateHandlerScope<A>.() -> Unit) = instanceOf(A::class.java, block)

    /**
     * Matches when the resolved state is an instance of [clazz].
     */
    inline fun <A : T> instanceOf(clazz: Class<out A>, block: StateHandlerScope<A>.() -> Unit) {
        val key = getIndex()
        addToMap(StateTester.Instance(clazz), key)
        routing.section(key) {
            val shs = StateHandlerScope(this, contextKey as AttrKey<StateHolder<A>>)
            shs.block()
        }
    }

    /**
     * Matches when the resolved state is equal to [value].
     */
    inline fun <A : T> valueOf(value: A, block: StateHandlerScope<A>.() -> Unit) {
        val key = getIndex()
        addToMap(StateTester.Value(value as Any), key)
        routing.section(key) {
            val shs = StateHandlerScope(this, contextKey as AttrKey<StateHolder<A>>)
            shs.block()
        }
    }

    /**
     * Matches when the checker returns true
     */
    inline fun check(crossinline checker: (T) -> Boolean, block: StateHandlerScope<T>.() -> Unit) {
        val key = getIndex()
        addToMap(object : StateTester.Checker() {
            override fun accept(obj: Any): Boolean {
                return checker(obj as T)
            }
        }, key)
        routing.section(key) {
            val shs = StateHandlerScope(this, contextKey)
            shs.block()
        }
    }

    inline fun default(block: StateHandlerScope<T>.() -> Unit) {
        routing.default {
            val shs = StateHandlerScope(this, contextKey)
            shs.block()
        }
    }

    fun getIndex(): Int {
        return index++
    }

    /**
     * Finalizes the routing.
     *
     * This is called automatically by [stateHandler].
     */
    fun build() {
        if (routing.default != null) {
            routing.keyGeneratorInt = createStateGen(true)
        }
        routing.build()
    }
}

class StateHolder<T>(val value: T)

/**
 * Scope used inside a matched state branch.
 */
class StateHandlerScope<T>(
    private val store: TransparentHandlerStore,
    val contextKey: AttrKey<StateHolder<T>>,
) : TransparentHandlerStore {
    /**
     * Returns the state value that matched the current branch.
     */
    fun HandlerContext.state(): T {
        return get(contextKey)?.value ?: error("No value for context key $contextKey")
    }

    override fun addHandler(handler: Handler) {
        store.addHandler(handler)
    }

    override val felineDispatcher: FelineDispatcher
        get() = store.felineDispatcher
}

sealed class StateTester {
    abstract fun accept(obj: Any): Boolean

    data class Value(val value: Any) : StateTester() {
        override fun accept(obj: Any): Boolean {
            return value == obj
        }
    }

    data class ByClass(val clazz: Class<*>) : StateTester() {
        init {
            clazz.requireInstantiable()
        }

        override fun accept(obj: Any): Boolean {
            return this.clazz === obj.javaClass
        }
    }

    data class Instance(val clazz: Class<*>) : StateTester() {
        override fun accept(obj: Any): Boolean {
            return this.clazz.isInstance(obj)
        }
    }

    abstract class Checker : StateTester()
}


internal class StateGeneratorInt<T>(
    private val stateFinder: StateFinder<T>,
    private val attrKey: AttrKey<StateHolder<T>>,
    map: LinkedHashMap<StateTester, Int>,
    canDefault: Boolean,
) : KeyGeneratorInt {
    private val intIdArray = map.map { it.value }.toIntArray()
    private val stateTesters = map.map { it.key }.toTypedArray()

    private val lastReturn = if (canDefault) Int.MAX_VALUE else Int.MIN_VALUE

    override fun generate(
        update: Update,
        handlerContext: HandlerContext,
    ): Int {
        val state = stateFinder.find(update, handlerContext) ?: return Int.MIN_VALUE
        for (i in stateTesters.indices) {
            if (stateTesters[i].accept(state)) {
                handlerContext[attrKey] = StateHolder(state)
                return intIdArray[i]
            }
        }
        return lastReturn
    }
}


private fun Class<*>.requireInstantiable() {
    if (isInterface || Modifier.isAbstract(modifiers)) {
        throw IllegalStateException("$this must not be abstract")
    }
}
