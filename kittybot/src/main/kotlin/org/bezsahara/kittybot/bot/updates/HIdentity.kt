package org.bezsahara.kittybot.bot.updates

import org.bezsahara.kittybot.bot.dispatchers.HandlerIdentity

sealed class HIdentity {
    abstract fun get(i: Int): Int

    class ByArray(
        private val startIdentity: Int,
        private val array: IntArray
    ) : HIdentity() {
        constructor(array: IntArray) : this(0, array)

        override fun get(i: Int): Int {
            val index = i - startIdentity
            if (index < 0) return -1
            if (index >= array.size) return -1
            return array[index]
        }
    }

    class ByMap(private val map: IntIntHashMap) : HIdentity() {
        override fun get(i: Int): Int {
            return map[i]
        }
    }

    companion object {
        private const val SMALL_ARRAY_SPAN = 32
        private const val ARRAY_SPREAD_FACTOR = 4

        internal fun create(mappings: HashMap<HandlerIdentity, Furball.AHandlerStore2>): HIdentity {
            if (mappings.isEmpty()) {
                return ByArray(intArrayOf(-1))
            }

            var minIdentity = Int.MAX_VALUE
            var maxIdentity = Int.MIN_VALUE

            mappings.keys.forEach { identity ->
                val value = identity.value
                if (value < minIdentity) minIdentity = value
                if (value > maxIdentity) maxIdentity = value
            }

            val span = maxIdentity - minIdentity + 1
            val maxArraySpan = mappings.size * ARRAY_SPREAD_FACTOR

            if (span <= SMALL_ARRAY_SPAN || span <= maxArraySpan) {
                val array = IntArray(span) { -1 }
                mappings.forEach { (key, value) ->
                    array[key.value - minIdentity] = value.arrayPos
                }
                return ByArray(minIdentity, array)
            }

            val finalMapMappings = IntIntHashMap(mappings.size, -1, 0.9)
            mappings.forEach { (key, value) ->
                finalMapMappings[key.value] = value.arrayPos
            }
            return ByMap(finalMapMappings)
        }
    }
}
