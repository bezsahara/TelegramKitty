package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import kotlin.collections.List
import kotlinx.serialization.Serializable


/**
 * This object contains information about the color scheme for a user's name, message replies and link previews based on a unique gift.
 * 
 * [link](https://core.telegram.org/bots/api#uniquegiftcolors): https://core.telegram.org/bots/api#uniquegiftcolors
 * 
 * @param modelCustomEmojiId Custom emoji identifier of the unique gift's model
 * @param symbolCustomEmojiId Custom emoji identifier of the unique gift's symbol
 * @param lightThemeMainColor Main color used in light themes; RGB format
 * @param lightThemeOtherColors List of 1-3 additional colors used in light themes; RGB format
 * @param darkThemeMainColor Main color used in dark themes; RGB format
 * @param darkThemeOtherColors List of 1-3 additional colors used in dark themes; RGB format
 */
@Serializable
data class UniqueGiftColors(
    @SerialName("model_custom_emoji_id") val modelCustomEmojiId: String,
    @SerialName("symbol_custom_emoji_id") val symbolCustomEmojiId: String,
    @SerialName("light_theme_main_color") val lightThemeMainColor: Long,
    @SerialName("light_theme_other_colors") val lightThemeOtherColors: List<Long>,
    @SerialName("dark_theme_main_color") val darkThemeMainColor: Long,
    @SerialName("dark_theme_other_colors") val darkThemeOtherColors: List<Long>
)

