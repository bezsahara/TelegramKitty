package org.bezsahara.kittybot.bot.action.route

import org.bezsahara.kittybot.bot.dispatchers.HandlerStore
import org.bezsahara.kittybot.bot.dispatchers.TransparentHandlerStore
import org.bezsahara.kittybot.telegram.classes.core.update.UpdateKind

@Deprecated("Use routing without updateKinds")
inline fun TransparentHandlerStore.routingInt(keyGeneratorInt: KeyGeneratorInt, updateKinds: Set<UpdateKind<*>>? = null, builder: RoutingStrategyInt.() -> Unit) {
    val rsa = RoutingStrategyInt(keyGeneratorInt, this, updateKinds)
    rsa.builder()
    rsa.build()
}
@Deprecated("Use routing without updateKinds")
inline fun <T> TransparentHandlerStore.routing(keyGeneratorAny: KeyGeneratorAny<T>, updateKinds: Set<UpdateKind<*>>? = null, builder: RoutingStrategyAny<T>.() -> Unit) {
    val rsa = RoutingStrategyAny(keyGeneratorAny, this, updateKinds)
    rsa.builder()
    rsa.build()
}

inline fun TransparentHandlerStore.routingInt(keyGeneratorInt: KeyGeneratorInt, builder: RoutingStrategyInt.() -> Unit) {
    val rsa = RoutingStrategyInt(keyGeneratorInt, this)
    rsa.builder()
    rsa.build()
}

inline fun <T> TransparentHandlerStore.routing(keyGeneratorAny: KeyGeneratorAny<T>, builder: RoutingStrategyAny<T>.() -> Unit) {
    val rsa = RoutingStrategyAny(keyGeneratorAny, this)
    rsa.builder()
    rsa.build()
}
