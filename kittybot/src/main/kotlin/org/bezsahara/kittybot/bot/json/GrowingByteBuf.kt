@file:Suppress("DuplicatedCode","INVISIBLE_REFERENCE", "NOTHING_TO_INLINE")

package org.bezsahara.kittybot.bot.json

import io.netty.buffer.Unpooled
import io.vertx.core.buffer.impl.BufferImpl
import io.vertx.core.internal.buffer.BufferInternal
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.internal.InternalJsonWriter
import kotlinx.serialization.json.internal.StreamingJsonEncoder
import kotlinx.serialization.json.internal.WriteMode
import org.bezsahara.kittybot.doubles.DoubleTransform


@OptIn(kotlinx.serialization.json.internal.JsonFriendModuleApi::class)
final class JsonByteBuffer(initialCapacity: Int) : InternalJsonWriter {
    constructor() : this(256)

    private var buf: ByteArray = ByteArray(initialCapacity)
    private var charBuf: CharArray = CharArrayPool.take() // can be dirty
    private var pos: Int = 0

    init {
        put('{'.code.toByte())
    }

    private inline fun ensure(n: Int) {
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

    private inline fun putUnsafe(b: Byte) {
        buf[pos++] = b
    }

    fun putComma() {
        put(','.code.toByte())
    }

    fun putQuote() {
        put('"'.code.toByte())
    }

    // Json ---
    fun putStringUnsafe(key: ByteArray, value: String) {
        stageBrackets {
            writeArr(key)
        }
        writeQuoted(value)
        putComma()
    }

    fun <T> putJsonObject(key: ByteArray, serializer: KSerializer<T>, value: T) {
        stageBrackets {
            writeArr(key)
        }
        encodeJsonObject(serializer, value)
        putComma()
    }

    fun putNumberUnsafe(key: ByteArray, v: Long) {
        stageBrackets {
            writeArr(key)
        }
        writeLong(v)
        putComma()
    }

    fun putNumberUnsafe(key: ByteArray, v: Double) {
        stageBrackets {
            writeArr(key)
        }
        writeDouble(v)
        putComma()
    }

    fun putListOfStringUnsafe(key: ByteArray, value: List<String>) = putList(key, value) { writeQuoted(it) }

    fun <T> putListOfJsonObjects(
        key: ByteArray,
        serializer: KSerializer<T>,
        values: List<T>,
    ) {
        putList(key, values) { encodeJsonObject(serializer, it) }
    }

    fun putListOfLongUnsafe(key: ByteArray, value: List<Long>) = putList(key, value) {
        writeLong(it)
    }

    fun putListOfDoubleUnsafe(key: ByteArray, value: List<Double>) = putList(key, value) {
        writeDouble(it)
    }

    private inline fun stageBrackets(block: () -> Unit) {
        ensure(1)
        buf[pos++] = QUOTE
        block()
        ensure(2)
        buf[pos++] = QUOTE
        buf[pos++] = DOUBLE_DOT
    }

    private inline fun <T> putList(key: ByteArray, list: List<T>, block: (T) -> Unit) {
        ensure(1)
        buf[pos++] = QUOTE
        writeArr(key)
        ensure(3)
        buf[pos++] = QUOTE
        buf[pos++] = DOUBLE_DOT
        buf[pos++] = BRACKET_LEFT
        val n = list.size
        if (n != 0) {
            var i = 0
            block(list[0])
            i++
            while (i < n) {
                put(','.code.toByte())
                block(list[i])
                i++
            }
        }
        ensure(1)
        buf[pos++] = BRACKET_RIGHT
        putComma()
    }

    fun putBoolUnsafe(key: ByteArray, boolean: Boolean) {
        stageBrackets { writeArr(key) }
        putBool(boolean)
        putComma()
    }

    // ---- booleans ----
    fun putBool(b: Boolean) {
        val p = pos;
        if (b) {
            ensure(4)
            val local = buf
            local[p] = 't'.code.toByte()
            local[p + 1] = 'r'.code.toByte()
            local[p + 2] = 'u'.code.toByte()
            local[p + 3] = 'e'.code.toByte()
            pos = p + 4
        } else {
            ensure(5)
            val local = buf
            local[p] = 'f'.code.toByte()
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
        buf[pos - 1] = '}'.code.toByte() // overwrite last comma
        return if (pos == buf.size)
            buf
        else {
            val newArray = ByteArray(pos)
            System.arraycopy(buf, 0, newArray, 0, pos)
            newArray
        }
    }

    fun toBuffer(): BufferInternal {
        if (pos == 1) {
            return BufferStat.EMPTY_JSON
        }
        buf[pos - 1] = '}'.code.toByte() // overwrite last comma
        return if (pos == buf.size) {
            BufferImpl(Unpooled.wrappedBuffer(buf))
        } else {
            val b = BufferImpl(pos)
            b.appendBytes(buf, 0, pos)
            b
        }
    }

    val size9: Int get() = pos

    fun reset(shrinkIfOver: Int = 1 shl 20) {
        if (buf.size > shrinkIfOver) buf = ByteArray(256)
        buf[0] = '{'.code.toByte()
        pos = 1
    }

    // ---- 1) write a single Char as UTF-8 (no quoting) ----
    override fun writeChar(char: Char) {
        val c = char.code
        when {
            c < 0x80 -> {
                put(c)
            }

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
        val length = text.length
        if (length == 0) return
        if (length < CHAR_STAGE_THRESHOLD) {
            writeDirect(text)
            return
        }

        ensureCharCapacity(length)
        text.toCharArray(charBuf, 0, 0, length)
        writeUtf8(charBuf, length)
    }

    private fun writeDirect(text: String) {
        var i = 0
        val length = text.length
        while (i < length) {
            // Burst an ASCII run
            if (text[i].code < 0x80) {
                ensure(length - i)
                val localBuf = buf
                var p = pos
                while (i < length) {
                    val ascii = text[i].code
                    if (ascii >= 0x80) break
                    localBuf[p++] = ascii.toByte()
                    i++
                }
                pos = p
                if (i >= length) break
            }
            // Non-ASCII code point
            i += writeCodePointUtf8(text, i)
        }
    }

    // ---- 3) write JSON-quoted string (with escapes) ----
    override fun writeQuoted(text: String) {
        val length = text.length
        if (length < QUOTED_CHAR_STAGE_THRESHOLD) {
            writeQuotedDirect(text)
            return
        }

        ensureCharCapacity(length + 2)
        val arr = charBuf
        arr[0] = QUOTE.toInt().toChar()
        text.toCharArray(arr, 1, 0, length)
        for (i in 1 until 1 + length) {
            val ch = arr[i].code
            if ((ch < ASCII_ESCAPE.size && ASCII_ESCAPE[ch] != 0.toByte()) || ch == 0x2028 || ch == 0x2029) {
                appendQuotedSlowPath(i, text)
                return
            }
        }
        arr[length + 1] = QUOTE.toInt().toChar()
        writeUtf8(arr, length + 2)
    }

    private fun writeQuotedDirect(text: String) {
        put(QUOTE)

        var i = 0
        val n = text.length

        while (i < n) {
            // Fast ASCII-safe burst
            if (text[i].code < 0x80 && ASCII_ESCAPE[text[i].code] == 0.toByte()) {
                ensure(n - i)
                val localBuf = buf
                var p = pos
                while (i < n) {
                    val c = text[i].code
                    if (c >= 0x80 || ASCII_ESCAPE[c] != 0.toByte()) break
                    localBuf[p++] = c.toByte()
                    i++
                }
                pos = p
                if (i >= n) break
            }

            val c = text[i].code

            when {
                c < 0x80 -> {
                    val esc = ASCII_ESCAPE[c]
                    if (esc == 1.toByte()) {
                        writeUnicodeEscape(c)   // \u00XX
                    } else {
                        ensure(2)
                        putUnsafe('\\'.code)
                        putUnsafe(esc)          // \" \\ \n \r \t \b \f
                    }
                    i++
                }

                c == 0x2028 || c == 0x2029 -> {
                    writeUnicodeEscape(c)
                    i++
                }

                else -> {
                    i += writeCodePointUtf8(text, i)
                }
            }
        }

        put(QUOTE)
    }

    private fun appendQuotedSlowPath(currentSize: Int, string: String) {
        var sz = currentSize
        for (i in currentSize - 1 until string.length) {
            sz = ensureTotalCharCapacity(sz, 2)
            val ch = string[i].code
            when {
                ch < ASCII_ESCAPE.size -> {
                    when (val marker = ASCII_ESCAPE[ch]) {
                        0.toByte() -> {
                            charBuf[sz++] = ch.toChar()
                        }

                        1.toByte() -> {
                            sz = appendUnicodeEscapeToCharBuffer(ch, sz)
                        }

                        else -> {
                            charBuf[sz] = '\\'
                            charBuf[sz + 1] = marker.toInt().toChar()
                            sz += 2
                        }
                    }
                }

                ch == 0x2028 || ch == 0x2029 -> {
                    sz = appendUnicodeEscapeToCharBuffer(ch, sz)
                }

                else -> {
                    charBuf[sz++] = ch.toChar()
                }
            }
        }
        sz = ensureTotalCharCapacity(sz, 1)
        charBuf[sz++] = QUOTE.toInt().toChar()
        writeUtf8(charBuf, sz)
    }

    // Encode one code point from s at index i as UTF-8; returns chars consumed (1 or 2).
    private fun writeCodePointUtf8(s: String, i0: Int): Int {
        val c = s[i0].code
        return when {
            c < 0x80 -> {
                put(c); 1
            }

            c < 0x800 -> {
                ensure(2)
                putUnsafe(0b1100_0000 or (c ushr 6))
                putUnsafe(0b1000_0000 or (c and 0x3F))
                1
            }

            c in 0xD800..0xDFFF -> {
                // Surrogate
                if (c > 0xDBFF || i0 + 1 >= s.length) {
                    put('?'.code.toByte()); 1
                } else {
                    val low = s[i0 + 1].code
                    if (low !in 0xDC00..0xDFFF) {
                        put('?'.code.toByte()); 1
                    } else {
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
        buf[pos++] = '\\'.code.toByte()
        buf[pos++] = 'u'.code.toByte()
        buf[pos++] = HEX[(code ushr 12) and 0xF]
        buf[pos++] = HEX[(code ushr 8) and 0xF]
        buf[pos++] = HEX[(code ushr 4) and 0xF]
        buf[pos++] = HEX[code and 0xF]
    }

    private inline fun writeArr(bytes: ByteArray) {
        val bs = bytes.size
        ensure(bs)
        System.arraycopy(bytes, 0, buf, pos, bs)
        pos += bs
    }

    private fun writeUtf8(chars: CharArray, count: Int) {
        var i = 0
        while (i < count) {
            val c = chars[i].code
            when {
                c < 0x80 -> {
                    ensure(count - i)
                    val localBuf = buf
                    var p = pos
                    while (i < count) {
                        val ascii = chars[i].code
                        if (ascii >= 0x80) break
                        localBuf[p++] = ascii.toByte()
                        i++
                    }
                    pos = p
                }

                c < 0x800 -> {
                    ensure(2)
                    putUnsafe(c shr 6 or 0xc0)
                    putUnsafe(c and 0x3f or 0x80)
                    i++
                }

                c < 0xd800 || c > 0xdfff -> {
                    ensure(3)
                    putUnsafe(c shr 12 or 0xe0)
                    putUnsafe(c shr 6 and 0x3f or 0x80)
                    putUnsafe(c and 0x3f or 0x80)
                    i++
                }

                else -> {
                    val low = if (i + 1 < count) chars[i + 1].code else 0
                    if (c > 0xdbff || low !in 0xdc00..0xdfff) {
                        put('?'.code)
                        i++
                    } else {
                        val codePoint = 0x010000 + ((c and 0x03ff) shl 10) + (low and 0x03ff)
                        ensure(4)
                        putUnsafe(codePoint shr 18 or 0xf0)
                        putUnsafe(codePoint shr 12 and 0x3f or 0x80)
                        putUnsafe(codePoint shr 6 and 0x3f or 0x80)
                        putUnsafe(codePoint and 0x3f or 0x80)
                        i += 2
                    }
                }
            }
        }
    }

    private fun ensureCharCapacity(expected: Int) {
        ensureTotalCharCapacity(0, expected)
    }

    private fun ensureTotalCharCapacity(oldSize: Int, additional: Int): Int {
        val newSize = oldSize + additional
        if (charBuf.size <= newSize) {
            charBuf = charBuf.copyOf(newSize.coerceAtLeast(oldSize * 2))
        }
        return oldSize
    }

    private fun appendUnicodeEscapeToCharBuffer(code: Int, start: Int): Int {
        var sz = ensureTotalCharCapacity(start, 6)
        charBuf[sz++] = '\\'
        charBuf[sz++] = 'u'
        charBuf[sz++] = HEX_CHARS[(code ushr 12) and 0xF]
        charBuf[sz++] = HEX_CHARS[(code ushr 8) and 0xF]
        charBuf[sz++] = HEX_CHARS[(code ushr 4) and 0xF]
        charBuf[sz++] = HEX_CHARS[code and 0xF]
        return sz
    }

    private var encoder: StreamingJsonEncoder? = null

    private fun <T> encodeJsonObject(serializer: KSerializer<T>, obj: T) {
        var enc = encoder
        if (enc == null) {
            enc = StreamingJsonEncoder(
                this, jsonInstance,
                WriteMode.OBJ,
                arrayOfNulls(WriteMode.entries.size)
            )
            encoder = enc
        }
        enc.encodeSerializableValue(serializer, obj)
    }

    override fun release() {}

    companion object {
        internal const val BRACKET_RIGHT = ']'.code.toByte()
        internal const val BRACKET_LEFT = '['.code.toByte()
        internal const val DOUBLE_DOT = ':'.code.toByte()
        internal const val QUOTE = '"'.code.toByte()

//        private val nullArray = "null".toCharArray()
        private val emptyJson = "{}".toByteArray()
        private val HEX: ByteArray = byteArrayOf(
            '0'.code.toByte(), '1'.code.toByte(), '2'.code.toByte(), '3'.code.toByte(),
            '4'.code.toByte(), '5'.code.toByte(), '6'.code.toByte(), '7'.code.toByte(),
            '8'.code.toByte(), '9'.code.toByte(), 'A'.code.toByte(), 'B'.code.toByte(),
            'C'.code.toByte(), 'D'.code.toByte(), 'E'.code.toByte(), 'F'.code.toByte()
        )
        private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
        private const val CHAR_STAGE_THRESHOLD = 32
        private const val QUOTED_CHAR_STAGE_THRESHOLD = 32

        private val ASCII_ESCAPE = ByteArray(128).apply {
            this['"'.code] = '"'.code.toByte()
            this['\\'.code] = '\\'.code.toByte()
            this['\b'.code] = 'b'.code.toByte()
            this['\n'.code] = 'n'.code.toByte()
            this['\r'.code] = 'r'.code.toByte()
            this['\t'.code] = 't'.code.toByte()
            this['\u000C'.code] = 'f'.code.toByte()

            var i = 0
            while (i < 0x20) {
                if (this[i] == 0.toByte()) this[i] = 1 // means \u00XX
                i++
            }
        }
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


internal object BufferStat {
    @JvmField
    val EMPTY_JSON: BufferInternal = BufferInternal.buffer(2).appendBytes("{}".toByteArray())
}

