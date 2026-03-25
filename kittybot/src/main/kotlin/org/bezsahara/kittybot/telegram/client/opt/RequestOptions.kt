package org.bezsahara.kittybot.telegram.client.opt

import org.bezsahara.kittybot.telegram.client.opt.RequestOptions.Companion.LARGE
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions.Companion.MEDIUM
import org.bezsahara.kittybot.telegram.client.opt.RequestOptions.Companion.SMALL


/**
 * Options that let the caller override the automatically predicted JSON buffer size
 * for a single request.
 *
 * Normally, each API method dynamically adjusts its internal byte buffer size based
 * on past usage — the more you call it, the more precisely it learns how big your
 * typical payloads are. This adaptive mechanism ensures that memory allocations
 * stay efficient over time.
 *
 * By providing a [RequestOptions] instance, you can **disable this dynamic behavior**
 * for that specific call: the value in [bufferSize] will be used directly as the
 * initial buffer capacity, and it will **not** be fed back into the predictor’s
 * size-learning algorithm.
 *
 * Use this only when you know in advance that a particular request will be
 * unusually large or small compared to normal traffic.
 *
 * Common presets:
 *  - [SMALL] → 512 bytes
 *  - [MEDIUM] → 4096 bytes (4 KB)
 *  - [LARGE] → 32768 bytes (32 KB)
 */
data class RequestOptions(@JvmField val bufferSize: Int) {

    companion object {
        @JvmField
        val SMALL = RequestOptions(512)

        @JvmField
        val MEDIUM = RequestOptions(4096)

        @JvmField
        val LARGE = RequestOptions(32768)
    }
}