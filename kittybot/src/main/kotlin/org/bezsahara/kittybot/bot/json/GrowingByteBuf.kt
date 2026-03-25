@file:Suppress("DuplicatedCode")

package org.bezsahara.kittybot.bot.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonUnquotedLiteral
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.internal.InternalJsonWriter
import kotlinx.serialization.json.internal.encodeByWriter
import org.bezsahara.kittybot.doubles.DoubleTransform

// IDK how a lot of stuff works here, half written by ChatGPT, but appears to work properly!
// Tested on latin and cyrillic chars
@Suppress("INVISIBLE_REFERENCE", "NOTHING_TO_INLINE")
@OptIn(kotlinx.serialization.json.internal.JsonFriendModuleApi::class)
final class JsonByteBuffer(initialCapacity: Int) : InternalJsonWriter, Appendable {
    constructor() : this(256)
    private var buf: ByteArray = ByteArray(initialCapacity)
    private var pos: Int = 0

    override fun append(csq: CharSequence?): JsonByteBuffer {
        if (csq == null) {
            putNull()
            return this
        }
        if (csq is String) {
            write(csq)
        } else {
            writeCsq(csq, 0, csq.length)
        }
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): JsonByteBuffer {
        if (csq == null) {
            writeArr(nullArray, start, end)
            return this
        }
        writeCsq(csq, start, end)
        return this
    }

    override fun append(c: Char): JsonByteBuffer {
        writeChar(c)
        return this
    }

    init {
        put('{'.code.toByte())
    }

    fun putNull() {
        ensure(4)
        val buffer = buf
        val position = pos
        pos += 4
        buffer[position    ] = 'n'.code.toByte()
        buffer[position + 1] = 'u'.code.toByte()
        buffer[position + 2] = 'l'.code.toByte()
        buffer[position + 3] = 'l'.code.toByte()
    }

    private fun ensure(n: Int) {
        if (buf.size - pos < n) grow(n)
    }

    private fun grow(n: Int) {
        val pss = buf.size
        val need = pos + n
        var cap = pss shl 1
        if (cap < need) cap = maxOf(need, pss + (pss ushr 1)) // ~1.5x min
        buf = buf.copyOf(cap)
//        println("Growing happens from ${pss} to ${buf.size}")
    }

    private inline fun put(b: Byte) {
        ensure(1)
        buf[pos++] = b
    }
    
    private inline fun put(b: Int) {
        ensure(1)
        buf[pos++] = b.toByte()
    }

    fun putUnsafeAccess(b: Byte) {
        buf[pos++] = b
    }

    fun ensureAccess(n: Int) = ensure(n)

    private inline fun putUnsafe(b: Int) {
        buf[pos++] = b.toByte()
    }

    fun putComma() {
        put(','.code.toByte())
    }

    private fun putAsciiRun(s: String, start: Int, end: Int) {
        val len = end - start
        ensure(len)
        var i = start
        var p = pos
        val local = buf
        while (i < end) {
            local[p++] = s[i++].code.toByte()
        }
        pos = p
    }

    private fun putAsciiRun(s: CharArray, start: Int, end: Int) {
        val len = end - start
        ensure(len)
        var i = start
        var p = pos
        val local = buf
        while (i < end) {
            local[p++] = s[i++].code.toByte()
        }
        pos = p
    }

    private fun putAsciiRun(s: CharSequence, start: Int, end: Int) {
        val len = end - start
        ensure(len)
        var i = start
        var p = pos
        val local = buf
        while (i < end) {
            local[p++] = s[i++].code.toByte()
        }
        pos = p
    }

    // Json ---
    fun putStringUnsafe(key: String, value: String) {
        ensure(1)
        buf[pos++] = QUOTE
        write(key)
        ensure(2)
        buf[pos++] = QUOTE
        buf[pos++] = DOUBLE_DOT
        writeQuoted(value)
        putComma()
    }

    fun <T> putJsonObject(key: String, serializer: KSerializer<T>, json: Json, value: T) {
        ensure(1)
        buf[pos++] = QUOTE
        write(key)
        ensure(2)
        buf[pos++] = QUOTE
        buf[pos++] = DOUBLE_DOT
        encodeByWriter(json, this, serializer, value)
        putComma()
    }

    fun putNumberUnsafe(key: String, v: Long) {
        ensure(1)
        buf[pos++] = QUOTE
        write(key)
        ensure(2)
        buf[pos++] = QUOTE
        buf[pos++] = DOUBLE_DOT
        writeLong(v)
        putComma()
    }

    fun putNumberUnsafe(key: String, v: Double) {
        ensure(1)
        buf[pos++] = QUOTE
        write(key)
        ensure(2)
        buf[pos++] = QUOTE
        buf[pos++] = DOUBLE_DOT
        writeDouble(v)
        putComma()
    }

    fun putListOfStringUnsafe(key: String, value: List<String>) {
        ensure(1)
        buf[pos++] = QUOTE
        write(key)
        ensure(3)
        val p = pos
        pos += 3
        buf[p] = QUOTE
        buf[p + 1] = DOUBLE_DOT
        buf[p + 2] = BRACKET_LEFT
        val n = value.size
        if (n != 0) {
            var i = 0
            writeQuoted(value[i])
            i++
            while (i < n) {
                put(','.code.toByte())
                writeQuoted(value[i])
                i++
            }
        }
        put(BRACKET_RIGHT)
        putComma()
    }

    fun <T> putListOfJsonObjects(
        key: String,
        serializer: KSerializer<T>,
        json: Json,
        values: List<T>
    ) {
        ensure(1)
        val buf = buf
        buf[pos++] = QUOTE
        write(key)
        ensure(3)
        val p = pos
        pos += 3
        buf[p] = QUOTE
        buf[p + 1] = DOUBLE_DOT
        buf[p + 2] = BRACKET_LEFT
        val n = values.size
        if (n != 0) {
            var i = 0
            encodeByWriter(json, this, serializer, values[0])
            i++
            while (i < n) {
                put(','.code.toByte())
                encodeByWriter(json, this, serializer, values[i])
                i++
            }
        }
        put(BRACKET_RIGHT)
        putComma()
    }

    fun putListOfNumberUnsafe(key: String, value: List<Number>) {
        put(QUOTE); write(key); put(QUOTE); put(DOUBLE_DOT); put(BRACKET_LEFT)
        val n = value.size
        if (n != 0) {
            var i = 0
            when (val firstValue = value[i]) {
                is Long -> {
                    writeLong(firstValue)
                    i++
                    while (i < n) {
                        put(','.code.toByte())
                        writeLong(value[i].toLong())
                        i++
                    }
                }
                is Double -> {
                    writeDouble(firstValue)
                    i++
                    while (i < n) {
                        put(','.code.toByte())
                        writeDouble(value[i].toDouble())
                        i++
                    }
                }
                else -> {
                    writeNumberElement(firstValue)
                    i++
                    while (i < n) {
                        put(','.code.toByte())
                        writeNumberElement(value[i])
                        i++
                    }
                }
            }
        }
        put(BRACKET_RIGHT)
        putComma()
    }

    /* --- helper for numbers in arrays --- */
    private fun writeNumberElement(num: Number) {
        when (num) {
            is Long -> writeLong(num)
            is Double -> writeDouble(num)
            is Int, is Short, is Byte -> writeLong(num.toLong())
            else -> {
                // Fallback: minimal alloc path
                val s = num.toString()
                putAsciiRun(s, 0, s.length)
            }
        }
    }
    
    fun putBoolUnsafe(key: String, boolean: Boolean) {
        put('\"'.code)
        write(key)
        put('\"'.code)
        put(DOUBLE_DOT)
        putBool(boolean)
        putComma()
    }

    // ---- booleans ----
    fun putBool(b: Boolean) {
        val p = pos; val local = buf
        if (b) {
            ensure(4)
            local[p    ] = 't'.code.toByte()
            local[p + 1] = 'r'.code.toByte()
            local[p + 2] = 'u'.code.toByte()
            local[p + 3] = 'e'.code.toByte()
            pos = p + 4
        } else {
            ensure(5)
            local[p    ] = 'f'.code.toByte()
            local[p + 1] = 'a'.code.toByte()
            local[p + 2] = 'l'.code.toByte()
            local[p + 3] = 's'.code.toByte()
            local[p + 4] = 'e'.code.toByte()
            pos = p + 5
        }
    }

    override fun writeLong(value: Long) {
        val size = stringSize(value)
        ensure(size)

        val start = pos
        var charPos = start + size
        var x = value
        val negative = x < 0
        if (!negative) x = -x // work in negative space to handle MIN_VALUE

        // write digits backwards using division in negative domain
        val buffer = buf
        while (x <= -10L) {
            val q = x / 10L
            val r = (q * 10L - x).toInt() // r in 0..9
            buffer[--charPos] = ('0'.code + r).toByte()
            x = q
        }
        buffer[--charPos] = ('0'.code + (-x).toInt()).toByte()
        if (negative) buffer[--charPos] = '-'.code.toByte()

        // charPos should now equal start
        pos = start + size
    }


    fun writeDouble(v: Double) {
        DoubleTransform.appendTo(v, this)
    }
    
    fun toByteArray(): ByteArray {
        if (pos == 1) {
            return emptyJson
        }
        buf[pos-1] = '}'.code.toByte() // overwrite last comma
        return if (pos == buf.size)
            buf
        else {
            val newArray = ByteArray(pos)
            System.arraycopy(buf, 0, newArray, 0, pos)
            newArray
        }
    }

    fun reset(shrinkIfOver: Int = 1 shl 20) {
        if (buf.size > shrinkIfOver) buf = ByteArray(256)
        pos = 0
    }

    fun putQuote() {
        ensure(1)
        buf[pos++] = QUOTE
    }

    // ---- 1) write a single Char as UTF-8 (no quoting) ----
    override fun writeChar(char: Char) {
        val c = char.code
        when {
            c < 0x80 -> { put(c) }
            c < 0x800 -> {
                ensure(2)
                putUnsafe(0b1100_0000 or (c ushr 6))
                putUnsafe(0b1000_0000 or (c and 0x3F))
            }
            c in 0xD800..0xDFFF -> {
                // Single surrogate is invalid; write replacement
                put('?'.code.toByte())
            }
            else -> {
                ensure(3)
                putUnsafe(0b1110_0000 or (c ushr 12))
                putUnsafe(0b1000_0000 or ((c ushr 6) and 0x3F))
                putUnsafe(0b1000_0000 or (c and 0x3F))
            }
        }
    }

    // ---- 2) write raw String as UTF-8 (no quoting) ----
    override fun write(text: String) {
        var i = 0
        val n = text.length
        while (i < n) {
            // Burst an ASCII run
            val start = i
            var end = start
            while (end < n) {
                if (text[end].code < 0x80) end++ else break
            }
            if (end > start) {
                putAsciiRun(text, start, end)
                i = end
                if (i >= n) break
            }
            // Non-ASCII code point
            i += writeCodePointUtf8(text, i)
        }
    }

    fun writeCsq(text: CharSequence, i: Int, n: Int) {
        var i = i
        while (i < n) {
            // Burst an ASCII run
            val start = i
            var end = start
            while (end < n) {
                val c = text[end].code
                if (c < 0x80) end++ else break
            }
            if (end > start) {
                putAsciiRun(text, start, end)
                i = end
                if (i >= n) break
            }
            // Non-ASCII code point
            i += writeCodePointUtf8(text, i)
        }
    }

    fun writeArr(text: CharArray, i: Int, n: Int) {
        var i = i
        while (i < n) {
            // Burst an ASCII run
            val start = i
            var end = start
            while (end < n) {
                val c = text[end].code
                if (c < 0x80) end++ else break
            }
            if (end > start) {
                putAsciiRun(text, start, end)
                i = end
                if (i >= n) break
            }
            // Non-ASCII code point
            i += writeCodePointUtf8(text, i)
        }
    }

    // ---- 3) write JSON-quoted string (with escapes) ----
    override fun writeQuoted(text: String) {
        put(QUOTE)
        val n = text.length
        var i = 0

        // Fast path: write contiguous "safe" chars (>= ' ', not \ or ", not U+2028/U+2029)
        while (i < n) {
            val ch = text[i]
            if (ch < ' ' || ch == '"' || ch == '\\' || ch == '\u2028' || ch == '\u2029') break

            // ASCII burst if possible
            if (ch.code < 0x80) {
                val start = i
                var end = start + 1
                while (end < n) {
                    val c2 = text[end]
                    if (c2.code >= 0x80 || c2 < ' ' || c2 == '"' || c2 == '\\' || c2 == '\u2028' || c2 == '\u2029') break
                    end++
                }
                putAsciiRun(text, start, end)
                i = end
            } else {
                i += writeCodePointUtf8(text, i)
            }
        }

        if (i == n) {
            put(QUOTE)
            return
        }

        // Slow path with escapes
        while (i < n) {
            val ch = text[i]
            when {
                ch == '"' -> { ensure(2); putUnsafe('\\'.code); putUnsafe('"'.code); i++ }
                ch == '\\' -> { ensure(2); putUnsafe('\\'.code); putUnsafe('\\'.code); i++ }
                ch < ' ' -> {
                    when (ch) {
                        '\b' -> { ensure(2); putUnsafe('\\'.code); putUnsafe('b'.code) }
                        '\n' -> { ensure(2); putUnsafe('\\'.code); putUnsafe('n'.code) }
                        '\r' -> { ensure(2); putUnsafe('\\'.code); putUnsafe('r'.code) }
                        '\t' -> { ensure(2); putUnsafe('\\'.code); putUnsafe('t'.code) }
                        '\u000C' -> { ensure(2); putUnsafe('\\'.code); putUnsafe('f'.code) }
                        else -> writeUnicodeEscape(ch.code)
                    }
                    i++
                }
                ch == '\u2028' || ch == '\u2029' -> { writeUnicodeEscape(ch.code); i++ }
                else -> {
                    // Non-ASCII safe char => UTF-8
                    i += writeCodePointUtf8(text, i)
                }
            }
        }
        put(QUOTE)
    }

    // Encode one code point from s at index i as UTF-8; returns chars consumed (1 or 2).
    private fun writeCodePointUtf8(s: String, i0: Int): Int {
        val c = s[i0].code
        return when {
            c < 0x80 -> { put(c); 1 }
            c < 0x800 -> {
                ensure(2)
                putUnsafe(0b1100_0000 or (c ushr 6))
                putUnsafe(0b1000_0000 or (c and 0x3F))
                1
            }
            c in 0xD800..0xDFFF -> {
                // Surrogate
                if (c > 0xDBFF || i0 + 1 >= s.length) { put('?'.code.toByte()); 1 }
                else {
                    val low = s[i0 + 1].code
                    if (low !in 0xDC00..0xDFFF) { put('?'.code.toByte()); 1 }
                    else {
                        val codePoint = 0x010000 + ((c and 0x03FF) shl 10) + (low and 0x03FF)
                        ensure(4)
                        val b = buf
                        val position = pos
                        pos += 4
                        b[position] = (0b11110_000 or (codePoint ushr 18)).toByte()
                        b[position + 1] = (0b1000_0000 or ((codePoint ushr 12) and 0x3F)).toByte()
                        b[position + 2] = (0b1000_0000 or ((codePoint ushr 6) and 0x3F)).toByte()
                        b[position + 3] = (0b1000_0000 or (codePoint and 0x3F)).toByte()
                        2
                    }
                }
            }
            else -> {
                ensure(3)
                putUnsafe(0b1110_0000 or (c ushr 12))
                putUnsafe(0b1000_0000 or ((c ushr 6) and 0x3F))
                putUnsafe(0b1000_0000 or (c and 0x3F))
                1
            }
        }
    }

    private fun writeCodePointUtf8(s: CharArray, i0: Int): Int {
        val c = s[i0].code
        return when {
            c < 0x80 -> { put(c); 1 }
            c < 0x800 -> {
                ensure(2)
                putUnsafe(0b1100_0000 or (c ushr 6))
                putUnsafe(0b1000_0000 or (c and 0x3F))
                1
            }
            c in 0xD800..0xDFFF -> {
                // Surrogate
                if (c > 0xDBFF || i0 + 1 >= s.size) { put('?'.code.toByte()); 1 }
                else {
                    val low = s[i0 + 1].code
                    if (low !in 0xDC00..0xDFFF) { put('?'.code.toByte()); 1 }
                    else {
                        val codePoint = 0x010000 + ((c and 0x03FF) shl 10) + (low and 0x03FF)
                        ensure(4)
                        val b = buf
                        val position = pos
                        pos += 4
                        b[position] = (0b11110_000 or (codePoint ushr 18)).toByte()
                        b[position + 1] = (0b1000_0000 or ((codePoint ushr 12) and 0x3F)).toByte()
                        b[position + 2] = (0b1000_0000 or ((codePoint ushr 6) and 0x3F)).toByte()
                        b[position + 3] = (0b1000_0000 or (codePoint and 0x3F)).toByte()
                        2
                    }
                }
            }
            else -> {
                ensure(3)
                putUnsafe(0b1110_0000 or (c ushr 12))
                putUnsafe(0b1000_0000 or ((c ushr 6) and 0x3F))
                putUnsafe(0b1000_0000 or (c and 0x3F))
                1
            }
        }
    }

    private fun writeCodePointUtf8(s: CharSequence, i0: Int): Int {
        val c = s[i0].code
        return when {
            c < 0x80 -> { put(c); 1 }
            c < 0x800 -> {
                ensure(2)
                putUnsafe(0b1100_0000 or (c ushr 6))
                putUnsafe(0b1000_0000 or (c and 0x3F))
                1
            }
            c in 0xD800..0xDFFF -> {
                // Surrogate
                if (c > 0xDBFF || i0 + 1 >= s.length) { put('?'.code.toByte()); 1 }
                else {
                    val low = s[i0 + 1].code
                    if (low !in 0xDC00..0xDFFF) { put('?'.code.toByte()); 1 }
                    else {
                        val codePoint = 0x010000 + ((c and 0x03FF) shl 10) + (low and 0x03FF)
                        ensure(4)
                        val b = buf
                        val position = pos
                        pos += 4
                        b[position] = (0b11110_000 or (codePoint ushr 18)).toByte()
                        b[position + 1] = (0b1000_0000 or ((codePoint ushr 12) and 0x3F)).toByte()
                        b[position + 2] = (0b1000_0000 or ((codePoint ushr 6) and 0x3F)).toByte()
                        b[position + 3] = (0b1000_0000 or (codePoint and 0x3F)).toByte()
                        2
                    }
                }
            }
            else -> {
                ensure(3)
                putUnsafe(0b1110_0000 or (c ushr 12))
                putUnsafe(0b1000_0000 or ((c ushr 6) and 0x3F))
                putUnsafe(0b1000_0000 or (c and 0x3F))
                1
            }
        }
    }

    private fun writeUnicodeEscape(code: Int) {
        ensure(6)
        putUnsafe('\\'.code); putUnsafe('u'.code)
        putUnsafe(HEX[(code ushr 12) and 0xF].toInt())
        putUnsafe(HEX[(code ushr 8) and 0xF].toInt())
        putUnsafe(HEX[(code ushr 4) and 0xF].toInt())
        putUnsafe(HEX[code and 0xF].toInt())
    }

    override fun release() {}

    companion object {
        internal const val BRACKET_RIGHT = ']'.code.toByte()
        internal const val BRACKET_LEFT = '['.code.toByte()
        internal const val DOUBLE_DOT = ':'.code.toByte()
        internal const val QUOTE = '"'.code.toByte()

        private val nullArray = "null".toCharArray()
        private val emptyJson = "{}".toByteArray()
        private val HEX: ByteArray = byteArrayOf(
            '0'.code.toByte(), '1'.code.toByte(), '2'.code.toByte(), '3'.code.toByte(),
            '4'.code.toByte(), '5'.code.toByte(), '6'.code.toByte(), '7'.code.toByte(),
            '8'.code.toByte(), '9'.code.toByte(), 'A'.code.toByte(), 'B'.code.toByte(),
            'C'.code.toByte(), 'D'.code.toByte(), 'E'.code.toByte(), 'F'.code.toByte()
        )
    }
}

internal fun stringSize(x0: Long): Int {
    var x = x0
    var d = 1
    if (x >= 0) {
        d = 0
        x = -x
    }
    var p = -10L
    var i = 1
    while (i < 19) {
        if (x > p) return i + d
        p *= 10L
        i++
    }
    return 19 + d
}






