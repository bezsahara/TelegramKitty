package org.bezsahara.samples

import org.bezsahara.kittybot.bot.KittyBot
import org.bezsahara.kittybot.bot.dispatchers.FelineDispatcher
import org.bezsahara.kittybot.bot.dispatchers.y.command
import org.bezsahara.kittybot.bot.dispatchers.y.scopes.chatId
import org.bezsahara.kittybot.telegram.classes.chat.ChatId
import org.bezsahara.kittybot.telegram.classes.message.Message
import org.bezsahara.kittybot.telegram.classes.rich.InputRichMessage
import org.bezsahara.kittybot.telegram.classes.rich.RichBlock
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockAnchor
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockAnimation
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockAudio
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockBlockQuotation
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockCaption
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockCollage
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockDetails
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockDivider
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockFooter
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockList
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockMap
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockMathematicalExpression
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockParagraph
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockPhoto
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockPreformatted
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockPullQuotation
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockSectionHeading
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockSlideshow
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockTable
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockThinking
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockVideo
import org.bezsahara.kittybot.telegram.classes.rich.RichBlockVoiceNote
import org.bezsahara.kittybot.telegram.classes.rich.RichMessage
import org.bezsahara.kittybot.telegram.classes.rich.RichText
import org.bezsahara.kittybot.telegram.classes.rich.RichTextAnchor
import org.bezsahara.kittybot.telegram.classes.rich.RichTextAnchorLink
import org.bezsahara.kittybot.telegram.classes.rich.RichTextArray
import org.bezsahara.kittybot.telegram.classes.rich.RichTextBankCardNumber
import org.bezsahara.kittybot.telegram.classes.rich.RichTextBold
import org.bezsahara.kittybot.telegram.classes.rich.RichTextBotCommand
import org.bezsahara.kittybot.telegram.classes.rich.RichTextCashtag
import org.bezsahara.kittybot.telegram.classes.rich.RichTextCode
import org.bezsahara.kittybot.telegram.classes.rich.RichTextCustomEmoji
import org.bezsahara.kittybot.telegram.classes.rich.RichTextDateTime
import org.bezsahara.kittybot.telegram.classes.rich.RichTextEmailAddress
import org.bezsahara.kittybot.telegram.classes.rich.RichTextHashtag
import org.bezsahara.kittybot.telegram.classes.rich.RichTextItalic
import org.bezsahara.kittybot.telegram.classes.rich.RichTextMarked
import org.bezsahara.kittybot.telegram.classes.rich.RichTextMathematicalExpression
import org.bezsahara.kittybot.telegram.classes.rich.RichTextMention
import org.bezsahara.kittybot.telegram.classes.rich.RichTextPhoneNumber
import org.bezsahara.kittybot.telegram.classes.rich.RichTextPlain
import org.bezsahara.kittybot.telegram.classes.rich.RichTextReference
import org.bezsahara.kittybot.telegram.classes.rich.RichTextReferenceLink
import org.bezsahara.kittybot.telegram.classes.rich.RichTextSpoiler
import org.bezsahara.kittybot.telegram.classes.rich.RichTextStrikethrough
import org.bezsahara.kittybot.telegram.classes.rich.RichTextSubscript
import org.bezsahara.kittybot.telegram.classes.rich.RichTextSuperscript
import org.bezsahara.kittybot.telegram.classes.rich.RichTextTextMention
import org.bezsahara.kittybot.telegram.classes.rich.RichTextUnderline
import org.bezsahara.kittybot.telegram.classes.rich.RichTextUrl
import org.bezsahara.kittybot.telegram.utils.unwrap
import org.intellij.lang.annotations.Language

/**
 * Rich messages have two shapes in the Bot API:
 *
 * - `InputRichMessage` is what you send. It contains either HTML or Markdown.
 * - `RichMessage` is what Telegram can return on `Message.richMessage`.
 *   It is a parsed tree: blocks contain rich text, and rich text can be a
 *   string, an array of rich text, or a tagged rich text object.
 */
fun FelineDispatcher.richTextExample() {
    command("/rich") {
        val sent = bot.sendRichMessageExample(chatId)
        bot.sendMessage(chatId, sent.describeRichMessage())
    }

    command("/rich_draft") {
        bot.sendRichMessageDraftExample(message.chat.id)
        bot.sendMessage(
            chatId,
            "Sent a rich-message draft update, then sent the final rich message."
        )
    }
}

suspend fun KittyBot.sendRichMessageExample(chatId: ChatId): Message {
    @Language("html")
    val html = """
        <h1>Daily build</h1>
        <p><b>Status:</b> green</p>
        <blockquote>Rich messages are sent as HTML or Markdown.</blockquote>
        <ul>
          <li>Use <code>InputRichMessage</code> for requests.</li>
          <li>Read <code>Message.richMessage</code> for Telegram's parsed result.</li>
        </ul>
    """.trimIndent()

    val sent = sendRichMessage(
        chatId = chatId,
        richMessage = InputRichMessage(html = html),
    ).unwrap()

    println(sent.describeRichMessage())
    return sent
}

suspend fun KittyBot.sendRichMessageDraftExample(privateChatId: Long) {
    val draftId = System.currentTimeMillis().coerceAtLeast(1L)

    sendRichMessageDraft(
        chatId = privateChatId,
        draftId = draftId,
        richMessage = InputRichMessage(
            html = """
                <p>Preparing report...</p>
                <tg-thinking>Checking the latest data</tg-thinking>
            """.trimIndent()
        ),
    ).unwrap()

    sendRichMessage(
        chatId = ChatId(privateChatId),
        richMessage = InputRichMessage(
            html = """
                <h2>Report ready</h2>
                <p>The final rich message replaces the temporary draft preview.</p>
            """.trimIndent()
        ),
    ).unwrap()
}

fun Message.describeRichMessage(): String {
    val richMessage = richMessage
        ?: return "This message does not contain Telegram rich-message content."

    return buildString {
        appendLine("Rich message:")
        appendLine("right-to-left: ${richMessage.isRtl == true}")
        richMessage.blocks.forEachIndexed { index, block ->
            appendLine("${index + 1}. ${block.describe()}")
        }
    }.trimEnd()
}

fun RichMessage.toPlainTextSummary(): String {
    return blocks.joinToString("\n") { block -> block.plainText() }
}

private fun RichBlock.describe(): String {
    return when (this) {
        is RichBlockParagraph -> "paragraph: ${text.plainText()}"
        is RichBlockSectionHeading -> "heading $size: ${text.plainText()}"
        is RichBlockPreformatted -> "preformatted${language?.let { " ($it)" } ?: ""}: ${text.plainText()}"
        is RichBlockFooter -> "footer: ${text.plainText()}"
        is RichBlockDivider -> "divider"
        is RichBlockMathematicalExpression -> "math block: $expression"
        is RichBlockAnchor -> "anchor: $name"
        is RichBlockList -> "list with ${items.size} item(s)"
        is RichBlockBlockQuotation -> "blockquote: ${blocks.joinToString(" ") { it.plainText() }}"
        is RichBlockPullQuotation -> "pull quote: ${text.plainText()}"
        is RichBlockCollage -> "collage with ${blocks.size} block(s)${caption.suffix()}"
        is RichBlockSlideshow -> "slideshow with ${blocks.size} slide(s)${caption.suffix()}"
        is RichBlockTable -> "table with ${cells.size} row(s)${caption?.let { ": ${it.plainText()}" } ?: ""}"
        is RichBlockDetails -> "details: ${summary.plainText()}"
        is RichBlockMap -> "map at ${location.latitude}, ${location.longitude}${caption.suffix()}"
        is RichBlockPhoto -> "photo${caption.suffix()}"
        is RichBlockAnimation -> "animation${caption.suffix()}"
        is RichBlockAudio -> "audio${caption.suffix()}"
        is RichBlockVideo -> "video${caption.suffix()}"
        is RichBlockVoiceNote -> "voice note${caption.suffix()}"
        is RichBlockThinking -> "thinking: ${text.plainText()}"
    }
}

private fun RichBlock.plainText(): String {
    return when (this) {
        is RichBlockParagraph -> text.plainText()
        is RichBlockSectionHeading -> text.plainText()
        is RichBlockPreformatted -> text.plainText()
        is RichBlockFooter -> text.plainText()
        is RichBlockMathematicalExpression -> expression
        is RichBlockAnchor -> name
        is RichBlockList -> items.joinToString("\n") { item ->
            "${item.label} ${item.blocks.joinToString(" ") { it.plainText() }}"
        }
        is RichBlockBlockQuotation -> blocks.joinToString(" ") { it.plainText() }
        is RichBlockPullQuotation -> text.plainText()
        is RichBlockCollage -> blocks.joinToString(" ") { it.plainText() }
        is RichBlockSlideshow -> blocks.joinToString(" ") { it.plainText() }
        is RichBlockTable -> buildList {
            cells.forEach { row ->
                add(row.mapNotNull { cell -> cell.text?.plainText() }.joinToString(" "))
            }
            caption?.let { add(it.plainText()) }
        }.filter { it.isNotBlank() }.joinToString("\n")
        is RichBlockDetails -> buildList {
            add(summary.plainText())
            blocks.forEach { add(it.plainText()) }
        }.filter { it.isNotBlank() }.joinToString("\n")
        is RichBlockDivider -> ""
        is RichBlockMap -> caption?.text?.plainText().orEmpty()
        is RichBlockThinking -> text.plainText()
        is RichBlockPhoto -> caption?.text?.plainText().orEmpty()
        is RichBlockAnimation -> caption?.text?.plainText().orEmpty()
        is RichBlockAudio -> caption?.text?.plainText().orEmpty()
        is RichBlockVideo -> caption?.text?.plainText().orEmpty()
        is RichBlockVoiceNote -> caption?.text?.plainText().orEmpty()
    }
}

private fun RichText.plainText(): String {
    return when (this) {
        is RichTextPlain -> value
        is RichTextArray -> value.joinToString("") { it.plainText() }
        is RichTextBold -> text.plainText()
        is RichTextItalic -> text.plainText()
        is RichTextUnderline -> text.plainText()
        is RichTextStrikethrough -> text.plainText()
        is RichTextSpoiler -> text.plainText()
        is RichTextDateTime -> text.plainText()
        is RichTextTextMention -> text.plainText()
        is RichTextSubscript -> text.plainText()
        is RichTextSuperscript -> text.plainText()
        is RichTextMarked -> text.plainText()
        is RichTextCode -> text.plainText()
        is RichTextCustomEmoji -> alternativeText
        is RichTextMathematicalExpression -> expression
        is RichTextUrl -> text.plainText()
        is RichTextEmailAddress -> text.plainText()
        is RichTextPhoneNumber -> text.plainText()
        is RichTextBankCardNumber -> text.plainText()
        is RichTextMention -> text.plainText()
        is RichTextHashtag -> text.plainText()
        is RichTextCashtag -> text.plainText()
        is RichTextBotCommand -> text.plainText()
        is RichTextAnchor -> name
        is RichTextAnchorLink -> text.plainText()
        is RichTextReference -> text.plainText()
        is RichTextReferenceLink -> text.plainText()
    }
}

private fun RichBlockCaption?.suffix(): String {
    return this?.let { ": ${it.text.plainText()}" }.orEmpty()
}
