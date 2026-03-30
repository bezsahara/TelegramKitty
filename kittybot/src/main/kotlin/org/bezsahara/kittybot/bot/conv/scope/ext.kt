package org.bezsahara.kittybot.bot.conv.scope

import org.bezsahara.kittybot.bot.conv.ConversationBuilder

fun ConversationBuilder.onStartCommand(block: suspend OnMsgScope.() -> Unit) {
    on(StartCommand, block)
}

fun ConversationBuilder.onText(match: MsgCheck, block: suspend OnMsgScope.() -> Unit) {
    on(msgCheckBuilder(match), block)
}

fun ConversationBuilder.onText(regex: Regex, block: suspend OnMsgScope.() -> Unit) {
    onText({ it.text?.let { t -> regex.matches(t) } ?: false }, block)
}

fun ConversationBuilder.onText(text: String, block: suspend OnMsgScope.() -> Unit) {
    onText({ it.text == text }, block)
}