package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.*
import org.bezsahara.kittybot.bot.dispatchers.Decision.Companion.CONSUMED
import org.bezsahara.kittybot.bot.dispatchers.Decision.Companion.NEXT
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds
import java.util.*

// Base class for handling updates
abstract class Furball(
    val bot: KittyBot,
    botDispatchers: FelineDispatcher,
    private val errorHandler: HandlerErrorHandler,
    private val furballConfig: FurballConfig
) {

    private val attrKeyMaxSize: Int
//    private val handlerContextBuilder: HandlerContextBuilder

    init {
        val identityScope = botDispatchers.identityScope
        val size = identityScope.highest()
        require(size < furballConfig.attrsLimit) { "You have a lot of Attribute Keys. Too much in fact. Are you sure u use them correctly? To remove this error set Furball.attrsLimit = [your number]" }
        attrKeyMaxSize = size
    }

    internal data class AHandlerStore2(val h: Handler, val arrayPos: Int)

    private val handlerListIdentity: HIdentity = botDispatchers.handlerList.let {
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

    // each piece is a number that indicates how far away is next handler with needed type
    private val handlerByKindMap = botDispatchers.handlerList.let { handlers ->
        val map = arrayOfNulls<IntArray>(23)

        fun Array<IntArray?>.fillIfEmpty(clazz: UpdateKind<*>): IntArray {
            var r = get(clazz.ordinal)
            if (r == null) {
                r = IntArray(handlers.size) { handlers.size - it }
                set(clazz.ordinal, r)
            }
            return r
        }

        val lastFilled = IdentityHashMap<UpdateKind<*>, Int>()

        handlers.forEachIndexed { index, handler ->
            val allowedTypes = handler.allowedKinds

            (allowedTypes ?: telegramUpdateKinds).forEach { kind ->
                val arr = map.fillIfEmpty(kind)

                // fill distances from (lastFilled+1) up to (index-1)
                val last = lastFilled[kind] ?: -1
                for (i in (last + 1) until index) {
                    val d = index - i
//                    if (d < arr[i])
                    arr[i] = d
                }

                // at the eligible index itself distance is 0
                arr[index] = 0
                lastFilled[kind] = index
            }
        }

        map
    }


    private val hlSize = botDispatchers.handlerList.size

    private val hopSafetyLimit = hlSize * furballConfig.hopSafetyTimes

    private val handlerList = botDispatchers.handlerList.let { hl ->
        Array(hl.size) {
            val h = hl[it]
            if (h is HandlerDelegate) {
                h.originalHandler
            } else {
                h
            }
        }
    }

    suspend fun applyHandlers(update: Update) {
        val jumpTable = handlerByKindMap[update.ordinal] ?: return
        var pos = jumpTable[0]
        val jumpTableSize = jumpTable.size
        val handlerContext = HandlerContextArray(attrKeyMaxSize)
        var hopSafety = 0
        while (true) {
            val handler = handlerList[pos]

            val res = try {
                handler.handleUpdate(update, bot, handlerContext)
            } catch (e: Throwable) {
                errorHandler.handleException(e, bot, update, handlerContext, handler)
            }

            if (hopSafety > hopSafetyLimit) {
                furballConfig.onRecursionProblem?.invoke() ?: throw HandlerException("It seems there is a recursion problem!")
            }

            when (res.result) {
                CONSUMED -> break
                NEXT -> {
                    pos += 1
                    if (jumpTableSize <= pos) break
                    pos += jumpTable[pos]
                    if (jumpTableSize <= pos) break
                    continue
                }

                else -> {
                    hopSafety += 1
                    pos = handlerListIdentity.get(res.result)
                    if (pos == -1) {
                        throw HandlerException("Did not find a handler `${res.result}`!")
                    }
                    if (res.offset != 0) {
                        pos += res.offset
                        if (pos < 0) {
                            throw HandlerException("pos is less than 0 after applying offset of ${res.offset}!")
                        }
                        if (jumpTableSize <= pos) break
                        continue
                    }
                }
            }

//          It is assumed user will know their code better
//            if (jumpTable[pos] != 0)
//                throw HandlerException("Handler ${handlerList[pos].identity} does not accept type ${update::class.java} so you can't jump here")
        }
    }

    abstract fun start()

}
