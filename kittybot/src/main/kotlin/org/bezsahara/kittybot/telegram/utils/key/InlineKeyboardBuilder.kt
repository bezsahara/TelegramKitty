package org.bezsahara.kittybot.telegram.utils.key

import org.bezsahara.kittybot.telegram.classes.games.CallbackGame
import org.bezsahara.kittybot.telegram.classes.keyboard.*
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo

inline fun buildInlineKeyboardMarkup(size: Int = 16, block: InlineKeyboardBuilder.() -> Unit): InlineKeyboardMarkup {
    val b = InlineKeyboardBuilder(size)
    b.block()
    return InlineKeyboardMarkup(b.build())
}

inline fun buildInlineKeyboard(block: InlineKeyboardBuilder.() -> Unit): List<List<InlineKeyboardButton>> {
    val b = InlineKeyboardBuilder()
    b.block()
    return b.build()
}

fun List<List<InlineKeyboardButton>>.toMarkup() = InlineKeyboardMarkup(this)

@Suppress("NOTHING_TO_INLINE")
class InlineKeyboardBuilder(size: Int) {
    constructor() : this(16)
    private val horizontal = ArrayList<List<InlineKeyboardButton>>(size)
    var vertical: ArrayList<InlineKeyboardButton>? = null
        private set

    fun addButton(inlineKeyboardButton: InlineKeyboardButton) {
        vertical?.let {
            it.add(inlineKeyboardButton)
            return
        }
        horizontal.add(listOf(inlineKeyboardButton))
    }

    fun addButtons(vararg inlineKeyboardButtons: InlineKeyboardButton) {
        vertical?.let {
            it.addAll(inlineKeyboardButtons.asList())
            return
        }
        horizontal.add(inlineKeyboardButtons.asList())
    }

    fun newVertical(size: Int): ArrayList<InlineKeyboardButton> {
        val a = ArrayList<InlineKeyboardButton>(size)
        vertical = a
        return a
    }

    fun resetVertical(a: ArrayList<InlineKeyboardButton>) {
        vertical = null
        horizontal.add(a)
    }

    inline fun addRow(size: Int, block: () -> Unit) {
        require(vertical == null) { "You can't create a row in another row!" }
        val buttons = newVertical(size)
        block()
        resetVertical(buttons)
    }

    inline fun addRow(block: () -> Unit) {
        require(vertical == null) { "You can't create a row in another row!" }
        val buttons = newVertical(6)
        block()
        resetVertical(buttons)
    }

    inline fun url(text: String, url: String) =
        addButton(InlineKeyboardButton.Url(text, url))

    inline fun callback(text: String, data: String) =
        addButton(InlineKeyboardButton.Callback(text, data))

    inline fun webApp(text: String, info: WebAppInfo) =
        addButton(InlineKeyboardButton.WebApp(text, info))

    inline fun loginUrl(text: String, login: LoginUrl) =
        addButton(InlineKeyboardButton.ILoginUrl(text, login))

    inline fun switchInline(text: String, query: String) =
        addButton(InlineKeyboardButton.SwitchInline(text, query))

    inline fun switchCurrent(text: String, query: String) =
        addButton(InlineKeyboardButton.SwitchCurrent(text, query))

    inline fun switchChosen(text: String, cfg: SwitchInlineQueryChosenChat) =
        addButton(InlineKeyboardButton.SwitchChosen(text, cfg))

    inline fun copyText(text: String, payload: CopyTextButton) =
        addButton(InlineKeyboardButton.CopyText(text, payload))

    // Overload for quick literal copy text
    inline fun copyText(text: String, copiedText: String) =
        addButton(InlineKeyboardButton.CopyText(text, CopyTextButton(copiedText)))

    inline fun callbackGame(text: String, game: CallbackGame) =
        addButton(InlineKeyboardButton.ICallbackGame(text, game))

    inline fun pay(text: String) =
        addButton(InlineKeyboardButton.Pay(text))

    // Optional sugar
    inline fun row(block: () -> Unit) =
        addRow { block() }

    inline fun row(vararg buttons: InlineKeyboardButton) =
        addButtons(*buttons)

    fun build(): List<List<InlineKeyboardButton>> {
        return horizontal
    }
}

