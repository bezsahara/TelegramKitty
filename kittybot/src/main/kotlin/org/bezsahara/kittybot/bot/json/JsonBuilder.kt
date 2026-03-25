package org.bezsahara.kittybot.bot.json


inline fun buildJson(body: UnsafeJsonBuilder.() -> Unit): String {
    val b = UnsafeJsonBuilder()
    b.body()
    return b.toString()
}

fun test() {
    buildJson {
    }
}

class UnsafeJsonBuilder {
    private val sb = StringBuilder()

    init { sb.append('{') }

    fun putNull(key: String) {
        sb.printQuoted(key).append(":null,")
    }

    fun putNullUnsafe(key: String) {
        sb.append('"').append(key).append('"').append(":null,")
    }

    fun putBoolean(key: String, boolean: Boolean) {
        sb.printQuoted(key).append(':').append(boolean)
    }

    fun putBooleanUnsafe(key: String, boolean: Boolean) {
        sb.append(key).append(':').append(boolean)
    }

    fun putString(key: String, value: String) {
        sb.printQuoted(key).append(':')
        sb.printQuoted(value).append(',')
    }

    fun putStringUnsafe(key: String, value: String) {
        val k = sb.append("asd")
        sb.append('"').append(key).append('"')
            .append(':')
        sb.printQuoted(value)
            .append(',')
    }

    fun putUnquoted(key: String, value: String) {
        sb.printQuoted(key).append(':').append(value).append(',')
    }

    fun putUnquotedUnsafe(key: String, value: String) {
        sb.append('"').append(key).append('"')
            .append(':')
            .append(value)
            .append(',')
    }

    fun putNumber(key: String, value: Long) {
        sb.printQuoted(key).append(':').append(value).append(',')
    }

    fun putNumberUnsafe(key: String, value: Long) {
        sb.append('"').append(key).append('"')
            .append(':')
            .append(value)
            .append(',')
    }

    fun putNumber(key: String, value: Float) {
        sb.printQuoted(key).append(':').append(value).append(',')
    }

    fun putNumberUnsafe(key: String, value: Float) {
        sb.append('"').append(key).append('"')
            .append(':')
            .append(value)
            .append(',')
    }

    fun putNumber(key: String, value: Double) {
        sb.printQuoted(key).append(':').append(value).append(',')
    }

    fun putNumberUnsafe(key: String, value: Double) {
        sb.append('"').append(key).append('"')
            .append(':')
            .append(value)
            .append(',')
    }

    fun putNumber(key: String, value: Int) {
        sb.printQuoted(key).append(':').append(value).append(',')
    }

    fun putNumberUnsafe(key: String, value: Int) {
        sb.append('"').append(key).append('"')
            .append(':')
            .append(value)
            .append(',')
    }

    override fun toString(): String {
        if (sb.length != 1) {
            sb.deleteCharAt(sb.length - 1) // remove last comma
        }
        sb.append('}')
        return sb.toString()
    }
}

private fun toHexChar(i: Int) : Char {
    val d = i and 0xf
    return if (d < 10) (d + '0'.code).toChar()
    else (d - 10 + 'a'.code).toChar()
}

private val ESCAPE_STRINGS: Array<String?> = arrayOfNulls<String>(93).apply {
    for (c in 0..0x1f) {
        val c1 = toHexChar(c shr 12)
        val c2 = toHexChar(c shr 8)
        val c3 = toHexChar(c shr 4)
        val c4 = toHexChar(c)
        this[c] = "\\u$c1$c2$c3$c4"
    }
    this['"'.code] = "\\\""
    this['\\'.code] = "\\\\"
    this['\t'.code] = "\\t"
    this['\b'.code] = "\\b"
    this['\n'.code] = "\\n"
    this['\r'.code] = "\\r"
    this[0x0c] = "\\f"
}

private fun StringBuilder.printQuoted(value: String): StringBuilder {
    append('"')
    var lastPos = 0
    for (i in value.indices) {
        val c = value[i].code
        if (c < ESCAPE_STRINGS.size && ESCAPE_STRINGS[c] != null) {
            append(value, lastPos, i) // flush prev
            append(ESCAPE_STRINGS[c])
            lastPos = i + 1
        }
    }

    if (lastPos != 0) append(value, lastPos, value.length)
    else append(value)
    return append('"')
}
