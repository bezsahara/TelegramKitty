package org.bezsahara.kittybot.bot.updates.furballs

import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.KittyBotResult
import org.bezsahara.kittybot.bot.action.dyn.findDynamicHandlers
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.Handler
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.real
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.*
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

// Base class for handling updates
class FurballDispatchers(
    val bot: KittyBot,
    private val channel: Channel<Update>,
    result: KittyBotResult
) : Furball() {

    private val attrKeyMaxSize: Int

    //    private val handlerContextBuilder: HandlerContextBuilder
    private val identityScope = result.identityScope
    private val errorHandler: HandlerErrorHandler = result.errorHandler
    private val furballConfig: FurballConfig = result.furballConfig


    init {
        telegramUpdateKinds
        val size = identityScope.close().highest()
        require(size < furballConfig.attrsLimit) { "You have a lot of Attribute Keys. Too much in fact. Are you sure u use them correctly? To remove this error set FurballConfig.attrsLimit = [your number]" }
        attrKeyMaxSize = size

        if (!furballConfig.ignoreIdentityDuplicated) {
            result.handlerList.checkIfIdentityDuplicated()?.let { (a, b) ->
                throw KittyError(
                    "You have duplicated identities in handler list! A<${a.real().javaClass.name}>: $a, B<${a.real().javaClass.name}>: $b." +
                            "\n to disable this set FurballConfig.ignoreIdentityDuplicated = true"
                )
            }
        }
    }

    internal data class AHandlerStore2(val h: Handler, val arrayPos: Int)

    private val handlerListIdentity: HIdentity = result.handlerList.let {
        val map = HashMap<HandlerIdentity, AHandlerStore2>()
        for (handlerIdx in it.indices) {
            val handler = it[handlerIdx]
            handler.testAndGetIdentity()?.let { identity ->
                if (map.containsKey(identity)) error("Two handlers have the same identity! Present is ${map[identity]}. Tried to add is $handler")
                map[identity] = AHandlerStore2(handler, handlerIdx)
            }
        }

        HIdentity.create(map)
    }

    // Last slot is the start slot. Other slots point to the next matching handler after that index.
    private val handlerByKindMap = result.handlerList.let { handlers ->
        val map = arrayOfNulls<IntArray>(telegramUpdateKinds.size)

        fun Array<IntArray?>.fillIfEmpty(clazz: UpdKind): IntArray {
            var r = get(clazz.ordinal)
            if (r == null) {
                r = IntArray(handlers.size + 1) { handlers.size }
                set(clazz.ordinal, r)
            }
            return r
        }

        val lastFilled = IntArray(telegramUpdateKinds.size) { -1 }

        handlers.forEachIndexed { index, handler ->

            (handler.allowedKinds ?: telegramUpdateKinds).forEach { kind ->
                val array = map.fillIfEmpty(kind)

                val last = lastFilled[kind.ordinal]
                lastFilled[kind.ordinal] = index
                if (last == -1) {
                    array[array.size - 1] = index
                    for (i in 0 until index) {
                        array[i] = index
                    }
                } else {
                    for (i in last until index) {
                        array[i] = index
                    }
                }
            }
        }
        map
    }

    private val hlSize = result.handlerList.size

    private val hopSafetyLimit = hlSize * furballConfig.hopSafetyTimes

    private val dynamic = findDynamicHandlers(result.handlerList)

    private val handlerList = result.handlerList.let { hl ->
        Array(hl.size) {
            hl[it].real()
        }
    }


    override suspend fun applyHandlers(update: Update) {
        val jumpTable = handlerByKindMap[update.ordinal] ?: return
        var pos = jumpTable[hlSize]
        if (hlSize <= pos) return
        val handlerContext = HandlerContextArray(attrKeyMaxSize, identityScope, channel)
        var hopSafety = 0
        while (true) {
            val handler = handlerList[pos]

            val res = try {
                handler.handleUpdate(update, bot, handlerContext)
            } catch (e: Throwable) {
                errorHandler.handleException(e, bot, update, handlerContext, handler)
            }

            if (hopSafety > hopSafetyLimit) {
                furballConfig.onRecursionProblem?.invoke(hopSafety)
                    ?: throw HandlerException("It seems there is a recursion problem!")
            }

            when (res.result) {
                Decision.CONSUMED -> break
                Decision.NEXT -> {
                    pos = jumpTable[pos]
                    if (hlSize <= pos) break
                    continue
                }

                else -> {
                    hopSafety += 1
                    pos = handlerListIdentity.get(res.result)
                    if (pos == -1) {
                        pos = maybeDynHI(res, handlerContext, update.ordinal)
                        if (pos != -1) continue
                        throw HandlerException("Did not find a handler `${res.result}`!")
                    }
                    if (res.offset != 0) {
                        pos += res.offset
                        if (pos < 0) {
                            throw HandlerException("pos is less than 0 after applying offset of ${res.offset}!")
                        }
                        if (hlSize <= pos) break
                    }
                    if (res.adjust) {
                        pos = if (pos == 0) jumpTable[hlSize] else jumpTable[pos - 1]
                        if (hlSize <= pos) break
                    }
                }
            }

//          It is assumed user will know their code better
//            if (jumpTable[pos] != 0)
//                throw HandlerException("Handler ${handlerList[pos].identity} does not accept type ${update::class.java} so you can't jump here")
        }
    }

    private fun maybeDynHI(decision: Decision, context: HandlerContext, updType: Int): Int {
        for (registry in dynamic) {
            val d = registry.createJumpDecision(context, decision, updType) ?: continue
            return handlerListIdentity.get(d.result)
        }
        return -1
    }
}