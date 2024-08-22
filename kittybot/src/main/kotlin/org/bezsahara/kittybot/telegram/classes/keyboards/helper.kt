package org.bezsahara.kittybot.telegram.classes.keyboards

class InlineKeyboardBuilder {
    private val vertical = mutableListOf<List<InlineKeyboardButton>>()

    class Row {
        private val horizontal = mutableListOf<InlineKeyboardButton>()

        fun button(b: InlineKeyboardButton) {
            horizontal.add(b)
        }

        fun button(text: String, callbackData: String) {
            horizontal.add(InlineKeyboardButton(text, callbackData = callbackData))
        }

        internal fun build(): List<InlineKeyboardButton> {
            return horizontal
        }
    }

    fun button(b: InlineKeyboardButton) {
        vertical.add(listOf(b))
    }

    fun button(text: String, callbackData: String) {
        vertical.add(listOf(InlineKeyboardButton(text, callbackData = callbackData)))
    }

    fun row(block: Row.() -> Unit) {
        val row = Row()
        row.block()
        vertical.add(row.build())
    }

    fun row(vararg buttons: InlineKeyboardButton) {
        vertical.add(buttons.toList())
    }

    fun build(): List<List<InlineKeyboardButton>> {
        return vertical
    }
}

class ReplyKeyboardBuilder {
    private val vertical = mutableListOf<List<KeyboardButton>>()

    class Row {
        private val horizontal = mutableListOf<KeyboardButton>()

        fun button(b: KeyboardButton) {
            horizontal.add(b)
        }

        fun button(text: String) {
            horizontal.add(KeyboardButton(text))
        }

        internal fun build(): List<KeyboardButton> {
            return horizontal
        }
    }

    fun button(b: KeyboardButton) {
        vertical.add(listOf(b))
    }

    fun button(text: String) {
        vertical.add(listOf(KeyboardButton(text)))
    }

    fun row(block: Row.() -> Unit) {
        val row = Row()
        row.block()
        vertical.add(row.build())
    }

    fun row(vararg buttons: KeyboardButton) {
        vertical.add(buttons.toList())
    }

    fun build(): List<List<KeyboardButton>> {
        return vertical
    }
}

inline fun inlineKeyboard(block: InlineKeyboardBuilder.() -> Unit): InlineKeyboardMarkup {
    return InlineKeyboardMarkup(InlineKeyboardBuilder().also(block).build())
}

inline fun replyKeyboard(
    isPersistent: Boolean? = null,
    resizeKeyboard: Boolean? = null,
    oneTimeKeyboard: Boolean? = null,
    inputFieldPlaceholder: String? = null,
    selective: Boolean? = null,
    block: ReplyKeyboardBuilder.() -> Unit
): ReplyKeyboardMarkup {
    return ReplyKeyboardMarkup(
        ReplyKeyboardBuilder().also(block).build(),
        isPersistent, resizeKeyboard, oneTimeKeyboard, inputFieldPlaceholder, selective
    )
}