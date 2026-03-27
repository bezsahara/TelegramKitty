package org.bezsahara.kittybot.bot.action.flow

import java.util.concurrent.ConcurrentHashMap


interface FlowIdentityStorage {
    operator fun set(identity: String, data: FlowPayload)

    operator fun get(identity: String): FlowPayload?

    fun remove(identity: String)
}

class FlowIdentityStorageInMem : FlowIdentityStorage {
    private val map = ConcurrentHashMap<String, FlowPayload>()

    override fun set(identity: String, data: FlowPayload) {
        map[identity] = data
    }

    override fun get(identity: String): FlowPayload? = map[identity]

    override fun remove(identity: String) {
        map.remove(identity)
    }
}
