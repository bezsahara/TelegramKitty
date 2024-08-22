package org.bezsahara.kittybot.telegram.utils


import kotlinx.serialization.Serializable



@Serializable
enum class ParseMode {
    HTML,
    Markdown,
    MarkdownV2
}

