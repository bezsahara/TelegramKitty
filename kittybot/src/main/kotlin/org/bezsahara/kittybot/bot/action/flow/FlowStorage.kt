package org.bezsahara.kittybot.bot.action.flow

import java.util.concurrent.ConcurrentHashMap


interface FlowIdentityStorage<T> {
    operator fun set(identity: String, data: FlowPayload<T>)

    operator fun get(identity: String): FlowPayload<T>?

    fun remove(identity: String)
}

class FlowIdentityStorageInMem<T> : FlowIdentityStorage<T> {
    private val map = ConcurrentHashMap<String, FlowPayload<T>>()

    override fun set(identity: String, data: FlowPayload<T>) {
        map[identity] = data
    }

    override fun get(identity: String): FlowPayload<T>? = map[identity]

    override fun remove(identity: String) {
        map.remove(identity)
    }
}
