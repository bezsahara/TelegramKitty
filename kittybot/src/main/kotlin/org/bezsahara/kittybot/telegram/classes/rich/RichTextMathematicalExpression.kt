package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A mathematical expression.
 *
 * [link](https://core.telegram.org/bots/api#richtextmathematicalexpression): https://core.telegram.org/bots/api#richtextmathematicalexpression
 *
 * @param type Type of the rich text, always "mathematical_expression"
 * @param expression The expression in LaTeX format
 */
@Serializable
data class RichTextMathematicalExpression(
    val expression: String
) : RichText {
    override val type: String = "mathematical_expression"
}
