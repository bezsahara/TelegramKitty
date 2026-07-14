package org.bezsahara.kittybot.bot.updates.furballs

import kotlinx.coroutines.channels.Channel
import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.KittyBotResult
import org.bezsahara.kittybot.bot.action.dyn.findDynamicHandlers
import org.bezsahara.kittybot.bot.dispatchers.Decision
import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity
import org.bezsahara.kittybot.bot.dispatchers.real
import org.bezsahara.kittybot.bot.errors.HandlerErrorHandler
import org.bezsahara.kittybot.bot.errors.KittyError
import org.bezsahara.kittybot.bot.updates.*
import org.bezsahara.kittybot.telegram.classes.core.update.UpdKind
import org.bezsahara.kittybot.telegram.classes.core.update.Update
import org.bezsahara.kittybot.telegram.classes.core.update.telegramUpdateKinds

// Base class for handling updates
abstract class FurballDispatchersCont(
    @JvmField
    val bot: KittyBot,
    @JvmField val channel: Channel<Update>,
    result: KittyBotResult
) : Furball() {

    @JvmField val attrKeyMaxSize: Int

    //    @JvmField val handlerContextBuilder: HandlerContextBuilder
    @JvmField val identityScope = result.identityScope
    @JvmField val errorHandler: HandlerErrorHandler = result.errorHandler
    @JvmField val furballConfig: FurballConfig = result.furballConfig


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

    @JvmField val handlerListIdentity: HIdentity = result.handlerList.let {
        val map = HashMap<HandlerIdentity, FurballDispatchers.AHandlerStore2>()
        for (handlerIdx in it.indices) {
            val handler = it[handlerIdx]
            handler.testAndGetIdentity()?.let { identity ->
                if (map.containsKey(identity) && !furballConfig.ignoreIdentityDuplicated) error("Two handlers have the same identity! Present is ${map[identity]}. Tried to add is $handler")
                map[identity] = FurballDispatchers.AHandlerStore2(handler, handlerIdx)
            }
        }

        HIdentity.create(map)
    }

    // Last slot is the start slot. Other slots point to the next matching handler after that index.
    @JvmField val handlerByKindMap = result.handlerList.let { handlers ->
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

    @JvmField val hlSize = result.handlerList.size

    @JvmField val hopSafetyLimit = hlSize * furballConfig.hopSafetyTimes

    @JvmField val dynamic = findDynamicHandlers(result.handlerList)

    @JvmField val handlerList = result.handlerList.let { hl ->
        Array(hl.size) {
            hl[it].real()
        }
    }

    protected fun maybeDynHI(decision: Decision, context: HandlerContext, updType: Int): Int {
        for (registry in dynamic) {
            val d = registry.createJumpDecision(context, decision, updType) ?: continue
            return handlerListIdentity.get(d.result)
        }
        return -1
    }
}
