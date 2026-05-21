package org.bezsahara.kittybot.bot.action.comms

import org.bezsahara.kittybot.bot.dispatchers.y.scopes.HandlerScope
import org.bezsahara.kittybot.telegram.classes.core.update.MessageUpdate
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType

/**
 * Reflection-backed command registry for a [CommandGroup].
 *
 * Typical usage is:
 * ```kotlin
 * class AdminCommands : CommandGroup {
 *     @BotCommand("Replies with pong")
 *     fun ping(): ExecBlock = ExecBlock {
 *         // use HandlerScope<MessageUpdate> here
 *     }
 *
 *     @BotCommand("Greets a user")
 *     fun greet(name: String): ExecBlock = ExecBlock {
 *         // `name` is parsed from `/greet "Alice"`
 *     }
 * }
 *
 * handlerStore.commandGroupHandler(AdminCommands())
 * ```
 *
 * Any function annotated with [BotCommand] is exposed as a telegram command whose name matches the
 * Kotlin method name. Annotated functions must return [ExecBlock]. Their parameters are inspected
 * when the store is created and later matched against parsed command arguments.
 *
 * Supported parameter types are [String], integer types mapped to [BotType.Long], floating-point
 * types mapped to [BotType.Double], and [List] of supported element types.
 *
 * In normal bot setup this store is created through
 * [org.bezsahara.kittybot.bot.action.comms.commandGroupHandler], which also registers the matching
 * [CommandGroupHandler] in a handler store.
 */

class CommandGroupStore(
    /**
     * Original command group instance whose annotated methods were scanned.
     */
    val commandGroup: CommandGroup,
    /**
     * Command name to overload map, keyed by parsed parameter signatures.
     */
    val executableMap: Map<String, Map<List<BotType>, MethodHandle>>,
    /**
     * Registered command descriptions keyed by command name without the leading `/`.
     */
    val commands: List<Pair<String, String>>, // Command (without /) to description
) {
    // Following are helper methods, however you can use fields however you like

    /**
     * Parses [line], resolves the matching executable, and runs it inside [scope].
     *
     * [line] should not include the leading `/`; for example, use `help` or `ban "user"`.
     */
    suspend fun locateExec(line: String, scope: HandlerScope<MessageUpdate>) {
        val executable = getExecutable(line)
        executable.apply {
            scope.apply {
                execute()
            }
        }
    }

    private fun getExecutable(line: String): ExecBlock {
        val res = ParsedBotCall.parse(line).unwrap()
        val paramsMap = executableMap[res.method.name]
            ?: error("MethodHandle not found! for descriptor of ${res.method.toDescString()}")
        val mh = paramsMap[res.method.args]
            ?: error("MethodHandle not found! for descriptor of ${res.method.toDescString()}")

        return getExecutable(mh, res.arguments)
    }

    fun getExecutable(mh: MethodHandle, args: Array<Any?>): ExecBlock {
        return (mh.invokeExact(args) as ExecBlock?)
            ?: throw NullPointerException("MethodHandle $mh returned null!")
    }

    fun getExecOrDefOrNull(mh: MethodHandle?, name: String, args: Array<Any?>): ExecBlock? {
        return if (mh == null) {
            if (commandGroup.canDefault) {
                commandGroup.catchDefault(name, args)
            } else null
        } else {
            getExecutable(mh, args)
        }
    }

    fun parse(line: String) = ParsedBotCall.parse(line)

    fun parseArgs(name: String, args: String) = ParsedBotCall.parseArgsOnly(name, args)

    companion object {
        /**
         * Creates a new store by scanning [commandGroup] for [BotCommand]-annotated methods.
         */
        fun create(commandGroup: CommandGroup): CommandGroupStore {
            val (commands, executableMap) = build(commandGroup)
            return CommandGroupStore(
                commandGroup, executableMap, commands
            )
        }

        private fun build(commandGroup: CommandGroup): Pair<List<Pair<String, String>>, Map<String, Map<List<BotType>, MethodHandle>>> {
            val lookup = MethodHandles.lookup()
            val clazz = commandGroup::class.java

            val commands = arrayListOf<Pair<String, String>>()
            val execMap = hashMapOf<String, HashMap<List<BotType>, MethodHandle>>()

            clazz.declaredMethods.forEach { method ->
                val annot = method.getAnnotation(BotCommand::class.java) ?: return@forEach
                val methodName = method.name
                require(method.returnType == ExecBlock::class.java) { "Method $methodName does not have return type of ExecBlock" }
                val params = method.parameters.map { param ->
                    botTypeOf(param.parameterizedType)
                }
                commands.add(methodName to annot.description)
                val nameMap = execMap.getOrPut(methodName) { hashMapOf() }
                val bound = lookup.unreflect(method).bindTo(commandGroup)
                nameMap[optimizeList(params)] = bound.asSpreader(Array<Any?>::class.java, params.size)
            }

            val finalExecMap = hashMapOf<String, Map<List<BotType>, MethodHandle>>()

            execMap.forEach { (key, map) ->
                finalExecMap[key] = if (map.size == 1) {
                    val mEntry = map.entries.single()
                    java.util.Collections.singletonMap(mEntry.key, mEntry.value)
                } else map
            }

            return commands to finalExecMap
        }

        private fun botTypeOf(type: Type): BotType {
            return when (type) {
                is Class<*> -> type.toBotType()
                is ParameterizedType -> {
                    val baseType = (type.rawType as Class<*>)
                    if (baseType != List::class.java) {
                        error("Only List types are supported with params")
                    }
                    BotType.List(botTypeOf(type.actualTypeArguments[0]))
                }

                is WildcardType -> {
                    botTypeOf(type.upperBounds.first())
                }

                else -> error("Unsupported type $type || ${type.javaClass}")
            }
        }

        private fun Class<*>.toBotType(): BotType {
            return when (this) {
                Long::class.java, Long::class.javaObjectType -> BotType.Long
                Double::class.java, Double::class.javaObjectType -> BotType.Double
                String::class.java -> BotType.String
                Void.TYPE -> BotType.Void
                else -> error("Bot type of not supported class $this")
            }
        }
    }
}

data class BotMethod(
    val name: String,
    val args: List<BotType>,
)

sealed interface BotType {
    data object Long : BotType
    data object Double : BotType
    data object String : BotType
    data object Void : BotType
    data class List(val type: BotType) : BotType
}

fun BotMethod.toDescString(): String {
    return "$name(${args.joinToString(", ") { it.toDescString() }})"
}

fun List<BotType>.toDescString(): String {
    return joinToString(", ", prefix = "(", postfix = ")") { it.toDescString() }
}

fun BotType.toDescString(): String {
    return when (this) {
        BotType.Double -> "Double"
        BotType.Long -> "Long"
        BotType.String -> "String"
        BotType.Void -> "V"
        is BotType.List -> "[${type.toDescString()}"
    }
}

private fun <T> optimizeList(list: List<T>): List<T> {
    if (list.isEmpty()) {
        return emptyList()
    }
    if (list.size == 1) {
        return listOf(list[0])
    }
    return list
}