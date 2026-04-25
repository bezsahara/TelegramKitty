package org.bezsahara.kittybot.telegram.utils.key

import org.bezsahara.kittybot.telegram.classes.keyboard.*
import org.bezsahara.kittybot.telegram.classes.webapp.WebAppInfo
import org.bezsahara.kittybot.telegram.values.KeyboardButtonStyle

fun buildReplyKeyboard(builder: KeyboardBuilder.() -> Unit): List<List<KeyboardButton>> {
    val b = KeyboardBuilder(); b.builder(); return b.build()
}

fun buildReplyKeyboardMarkup(
    isPersistent: Boolean? = null,
    resizeKeyboard: Boolean? = null,
    oneTimeKeyboard: Boolean? = null,
    inputFieldPlaceholder: String? = null,
    selective: Boolean? = null,
    builder: KeyboardBuilder.() -> Unit,
): ReplyKeyboardMarkup {
    val b = KeyboardBuilder(); b.builder()
    return ReplyKeyboardMarkup(
        b.build(),
        isPersistent,
        resizeKeyboard,
        oneTimeKeyboard,
        inputFieldPlaceholder,
        selective,
    )
}

class KeyboardBuilder() {
    private val horizontal = arrayListOf<List<KeyboardButton>>()
    var vertical: ArrayList<KeyboardButton>? = null
        private set

    fun build(): List<List<KeyboardButton>> {
        return horizontal
    }

    fun addButton(keyboardButton: KeyboardButton) {
        vertical?.let {
            it.add(keyboardButton)
            return
        }
        horizontal.add(listOf(keyboardButton))
    }

    fun addButtons(vararg keyboardButtons: KeyboardButton) {
        vertical?.let {
            keyboardButtons.forEach { i -> it.add(i) }
            return
        }
        horizontal.add(keyboardButtons.asList())
    }

    fun newVertical(): ArrayList<KeyboardButton> {
        val a = arrayListOf<KeyboardButton>()
        vertical = a
        return a
    }

    fun resetVertical(a: ArrayList<KeyboardButton>) {
        vertical = null
        horizontal.add(a)
    }

    inline fun addRow(block: () -> Unit) {
        require(vertical == null) { "You can't create a row in another row!" }
        val buttons = newVertical()
        block()
        resetVertical(buttons)
    }

    fun text(text: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(KeyboardButton(text, iconCustomEmojiId, style))

    fun users(text: String, req: KeyboardButtonRequestUsers, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(KeyboardButton(text, iconCustomEmojiId, style, requestUsers = req))

    fun chat(text: String, req: KeyboardButtonRequestChat, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(KeyboardButton(text, iconCustomEmojiId, style, requestChat = req))

    fun contact(text: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(KeyboardButton(text, iconCustomEmojiId, style, requestContact = true))

    fun location(text: String, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(KeyboardButton(text, iconCustomEmojiId, style, requestLocation = true))

    fun poll(text: String, type: KeyboardButtonPollType, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(KeyboardButton(text, iconCustomEmojiId, style, requestPoll = type))

    fun webApp(text: String, info: WebAppInfo, iconCustomEmojiId: String? = null, style: KeyboardButtonStyle? = null) =
        addButton(KeyboardButton(text, iconCustomEmojiId, style, webApp = info))

    // Optional sugar to match .row(...) overloads
    inline fun row(block: () -> Unit) =
        addRow { block() }

    inline fun row(vararg buttons: KeyboardButton) =
        addButtons(*buttons)
}