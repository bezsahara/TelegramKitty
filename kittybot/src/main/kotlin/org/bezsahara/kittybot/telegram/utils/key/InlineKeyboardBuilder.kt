package org.bezsahara.kittybot.telegram.utils.key

import org.bezsahara.kittybot.telegram.classes.games.CallbackGame
import org.bezsahara.kittybot.telegram.classes.keyboard.*
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo
import org.bezsahara.kittybot.telegram.values.KeyboardButtonStyle

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
    constructor() : this(8)

    private val horizontal = ArrayList<List<InlineKeyboardButton>>(size)
    private var vertical: ArrayList<InlineKeyboardButton>? = null

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

    fun addButtons(inlineKeyboardButtons: List<InlineKeyboardButton>) {
        vertical?.let {
            it.addAll(inlineKeyboardButtons)
            return
        }
        horizontal.add(inlineKeyboardButtons)
    }

    fun newVertical(size: Int) {
        require(vertical == null) { "You can't create a row in another row!" }
        vertical = ArrayList<InlineKeyboardButton>(size)
    }

    fun resetVertical() {
        horizontal.add(vertical ?: error("You need to create a raw to close it!"))
        vertical = null
    }

    inline fun addRow(size: Int = 6, block: () -> Unit) {
        newVertical(size)
        try {
            block()
        } finally {
            resetVertical()
        }
    }

    inline fun url(text: String, url: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, url = url))

    inline fun callback(text: String, data: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, callbackData = data))

    inline fun webApp(text: String, info: WebAppInfo, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, webApp = info))

    inline fun loginUrl(text: String, login: LoginUrl, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, loginUrl = login))

    inline fun switchInline(text: String, query: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, switchInlineQuery = query))

    inline fun switchCurrent(text: String, query: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, switchInlineQueryCurrentChat = query))

    inline fun switchChosen(
        text: String,
        cfg: SwitchInlineQueryChosenChat,
        iconCustomEmojiId: String? = null,
        style: KeyboardButtonStyle? = null,
    ) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, switchInlineQueryChosenChat = cfg))

    inline fun copyText(
        text: String,
        payload: CopyTextButton,
        iconCustomEmojiId: String? = null,
        style: KeyboardButtonStyle? = null,
    ) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, copyText = payload))

    // Overload for quick literal copy text
    inline fun copyText(text: String, copiedText: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, copyText = CopyTextButton(copiedText)))

    inline fun callbackGame(
        text: String,
        game: CallbackGame,
        iconCustomEmojiId: String? = null,
        style: KeyboardButtonStyle? = null,
    ) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, callbackGame = game))

    inline fun pay(text: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(InlineKeyboardButton(text, iconCustomEmojiId, style, pay = true))

    // Optional sugar
    inline fun row(block: () -> Unit) =
        addRow { block() }

    inline fun row(vararg buttons: InlineKeyboardButton) =
        addButtons(*buttons)

    fun build(): List<List<InlineKeyboardButton>> {
        return horizontal
    }
}

