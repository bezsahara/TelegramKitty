package org.bezsahara.kittybot.bot.action.comms

import java.util.Arrays

sealed interface ParsedBotResult

data class ParsedBotError(
    val error: String,
    val atIndex: Int?
) : ParsedBotResult, Exception(null, null, false, false)

data class ParsedBotCall(
    val method: BotMethod,
    val arguments: Array<Any?>,
) : ParsedBotResult {
    companion object {
        fun parse(line: String): ParsedBotResult {
            return BotCallParser(line).parse()
        }

        fun parseArgsOnly(name: String, args: String): ParsedBotResult {
            return BotCallParser(args).parseArgsOnly(name)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParsedBotCall

        if (method != other.method) return false
        if (!arguments.contentEquals(other.arguments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = method.hashCode()
        result = 31 * result + arguments.contentHashCode()
        return result
    }
}

fun ParsedBotResult.unwrap(): ParsedBotCall {
    return this as? ParsedBotCall ?: error(this)
}

private fun coerceToDeclaredType(value: Any?, from: BotType, to: BotType): Any? = when {
    from == to -> value
    from == BotType.Long && to == BotType.Double -> (value as kotlin.Long).toDouble()
    from is BotType.List && to is BotType.List -> {
        val items = value as List<*>
        items.map { coerceToDeclaredType(it, from.type, to.type) }
    }
    else -> throw IllegalArgumentException("Cannot use $from where $to is expected")
}

private enum class SeparatorMode {
    COMMA,
    WHITESPACE,
}

private data class TypedValue(
    val value: Any?,
    val type: BotType,
)

private class BotCallParser(private val text: String) {
    private var index: Int = 0
    private var failure: ParsedBotError? = null

    fun parseArgsOnly(name: String): ParsedBotResult {
        skipWhitespace()
        val args = when {
            isEof() -> emptyList()
            else -> parseTopLevelArguments() ?: return currentFailure()
        }

        skipWhitespace()
        if (!isEof()) {
            return failResult("Unexpected trailing input")
        }

        return ParsedBotCall(
            method = BotMethod(
                name = name,
                args = args.mapAsList { it.type }
            ),
            arguments = Array(args.size) { i -> args[i].value }
        )
    }

    fun parse(): ParsedBotResult {
        skipWhitespace()
        val methodName = parseMethodName() ?: return currentFailure()

        val whitespaceAfterName = skipWhitespace()
        val args = when {
            isEof() -> emptyList()
            whitespaceAfterName == 0 -> return failResult("Expected whitespace after method name")
            else -> parseTopLevelArguments() ?: return currentFailure()
        }

        skipWhitespace()
        if (!isEof()) {
            return failResult("Unexpected trailing input")
        }

        return ParsedBotCall(
            method = BotMethod(
                name = methodName,
                args = args.mapAsList { value ->
                    value.type
                },
            ),
            arguments = Array(args.size) { i -> args[i].value }
        )
    }

    private fun parseTopLevelArguments(): List<TypedValue>? {
        val args = mutableListOf<TypedValue>()
        args += parseValue() ?: return null

        var separatorMode: SeparatorMode? = null

        while (true) {
            val whitespaceCount = skipWhitespace()
            if (isEof()) {
                return args
            }

            when (separatorMode) {
                null -> {
                    if (peek() == ',') {
                        separatorMode = SeparatorMode.COMMA
                        index++
                        skipWhitespace()
                        if (!ensureValueCanStart("Expected argument after ','")) {
                            return null
                        }
                        args += parseValue() ?: return null
                    } else {
                        if (whitespaceCount == 0) {
                            return failStep("Expected whitespace or ',' between arguments")
                        }
                        separatorMode = SeparatorMode.WHITESPACE
                        args += parseValue() ?: return null
                    }
                }

                SeparatorMode.COMMA -> {
                    if (peek() != ',') {
                        return failStep("After using ',' once, all later top-level arguments must also use ','")
                    }
                    index++
                    skipWhitespace()
                    if (!ensureValueCanStart("Expected argument after ','")) {
                        return null
                    }
                    args += parseValue() ?: return null
                }

                SeparatorMode.WHITESPACE -> {
                    if (peek() == ',') {
                        return failStep("Commas are not allowed after choosing whitespace-separated arguments")
                    }
                    if (whitespaceCount == 0) {
                        return failStep("Expected whitespace between arguments")
                    }
                    args += parseValue() ?: return null
                }
            }
        }
    }

    private fun parseValue(): TypedValue? {
        if (isEof()) {
            return failStep("Expected value")
        }

        return when (peek()) {
            '"' -> {
                val value = parseQuotedString() ?: return null
                TypedValue(value, BotType.String)
            }
            '[' -> parseList()
            else -> parseAtom()
        }
    }

    private fun parseQuotedString(): String? {
        if (!expect('"')) {
            return null
        }
        val out = StringBuilder()

        while (!isEof()) {
            val ch = text[index++]
            when (ch) {
                '"' -> return out.toString()
                '\\' -> {
                    if (isEof()) {
                        return failStep("Unterminated escape sequence")
                    }
                    val escaped = text[index++]
                    out.append(
                        when (escaped) {
                            '"' -> '"'
                            '\\' -> '\\'
                            'n' -> '\n'
                            'r' -> '\r'
                            't' -> '\t'
                            else -> escaped
                        }
                    )
                }
                else -> out.append(ch)
            }
        }

        return failStep("Unterminated string literal")
    }

    private fun parseList(): TypedValue? {
        if (!expect('[')) {
            return null
        }
        skipWhitespace()

        if (!isEof() && peek() == ']') {
            index++
            return TypedValue(emptyList<Any?>(), BotType.List(BotType.Void))
        }

        val items = mutableListOf<TypedValue>()
        items += parseValue() ?: return null

        while (true) {
            skipWhitespace()
            if (isEof()) {
                return failStep("Unterminated list")
            }

            when (peek()) {
                ']' -> {
                    index++
                    break
                }
                ',' -> {
                    index++
                    skipWhitespace()
                    if (!ensureValueCanStart("Expected list item after ','")) {
                        return null
                    }
                    items += parseValue() ?: return null
                }
                else -> return failStep("List items must be separated by ','")
            }
        }

        var commonItemType: BotType = BotType.Void
        for (item in items) {
            commonItemType = mergeTypes(commonItemType, item.type) ?: return null
        }

        val normalizedItems = mutableListOf<Any?>()
        for (item in items) {
            normalizedItems += normalizeValue(item.value, item.type, commonItemType)
            if (failure != null) {
                return null
            }
        }

        return TypedValue(
            value = normalizedItems,
            type = BotType.List(commonItemType),
        )
    }

    private fun parseAtom(): TypedValue? {
        val start = index
        while (!isEof()) {
            val ch = peek()
            if (ch.isWhitespace() || ch == ',' || ch == ']') {
                break
            }
            index++
        }

        if (start == index) {
            return failStep("Expected value")
        }

        val token = text.substring(start, index)

        token.toLongOrNull()?.let { return TypedValue(it, BotType.Long) }
        token.toDoubleOrNull()?.let { return TypedValue(it, BotType.Double) }
        return TypedValue(token, BotType.String)
    }

    private fun parseMethodName(): String? {
        val start = index
        while (!isEof() && !peek().isWhitespace()) {
            index++
        }

        if (start == index) {
            return failStep("Expected method name")
        }

        val name = text.substring(start, index)
        if (name.any { it == '"' || it == '[' || it == ']' || it == ',' }) {
            return failStep("Invalid method name: '$name'")
        }
        return name
    }

    private fun mergeTypes(left: BotType, right: BotType): BotType? = when {
        left == right -> left
        left == BotType.Void -> right
        right == BotType.Void -> left
        (left == BotType.Long && right == BotType.Double) ||
                (left == BotType.Double && right == BotType.Long) -> BotType.Double
        left is BotType.List && right is BotType.List -> {
            val nestedType = mergeTypes(left.type, right.type) ?: return null
            BotType.List(nestedType)
        }
        else -> failStep("Incompatible list item types: $left and $right")
    }

    private fun normalizeValue(value: Any?, from: BotType, to: BotType): Any? = when {
        from == to -> value
        from == BotType.Long && to == BotType.Double -> (value as kotlin.Long).toDouble()
        from is BotType.List && to is BotType.List -> {
            val items = value as List<*>
            val normalized = mutableListOf<Any?>()
            for (item in items) {
                normalized += normalizeValue(item, from.type, to.type)
                if (failure != null) {
                    return null
                }
            }
            normalized
        }
        else -> failStep("Cannot normalize value from $from to $to")
    }

    private fun ensureValueCanStart(message: String): Boolean {
        if (isEof() || peek() == ',' || peek() == ']') {
            failStep<Unit>(message)
            return false
        }
        return true
    }

    private fun skipWhitespace(): Int {
        val start = index
        while (!isEof() && peek().isWhitespace()) {
            index++
        }
        return index - start
    }

    private fun expect(ch: Char): Boolean {
        if (isEof() || peek() != ch) {
            failStep<Unit>("Expected '$ch'")
            return false
        }
        index++
        return true
    }

    private fun peek(): Char = text[index]

    private fun isEof(): Boolean = index >= text.length

    private fun currentFailure(): ParsedBotError {
        return failure ?: ParsedBotError(
            error = "Unknown parse error",
            atIndex = index,
        ).also {
            failure = it
        }
    }

    private fun failResult(message: String): ParsedBotError {
        return recordFailure(message)
    }

    private fun <T> failStep(message: String): T? {
        recordFailure(message)
        return null
    }

    private fun recordFailure(message: String): ParsedBotError {
        failure?.let { return it }
        return ParsedBotError(
            error = message,
            atIndex = index,
        ).also {
            failure = it
        }
    }
}


inline fun <T, reified R> List<T>.mapAsList(transform: (T) -> R): List<R> {
    return Array<R>(size) {
        transform(get(it))
    }.asList()
}