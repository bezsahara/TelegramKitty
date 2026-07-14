package org.bezsahara.kittybot.telegram.classes.rich

import kotlinx.serialization.Serializable


/**
 * A block with a mathematical expression in LaTeX format, corresponding to the custom HTML tag <tg-math-block>.
 *
 * [link](https://core.telegram.org/bots/api#richblockmathematicalexpression): https://core.telegram.org/bots/api#richblockmathematicalexpression
 *
 * @param type Type of the block, always "mathematical_expression"
 * @param expression The mathematical expression in LaTeX format
 */
@Serializable
data class RichBlockMathematicalExpression(
    val expression: String
) : RichBlock {
    override val type: String = "mathematical_expression"
}
