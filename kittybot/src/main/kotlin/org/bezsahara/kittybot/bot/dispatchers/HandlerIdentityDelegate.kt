package org.bezsahara.kittybot.bot.dispatchers

import kotlin.reflect.KProperty

class HandlerIdentityDelegate() {
    private var value: HandlerIdentity = HandlerIdentity.emptyID

    operator fun getValue(thisRef: Any?, property: KProperty<*>): HandlerIdentity {
        return synchronized(this) {
            if (value == HandlerIdentity.emptyID) {
                value = HandlerIdentity.createNew()
            }
            value
        }
    }
}