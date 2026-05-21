package org.bezsahara.kittybot.telegram.classes.gifts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Contains the list of gifts received and owned by a user or a chat.
 * 
 * [link](https://core.telegram.org/bots/api#ownedgifts): https://core.telegram.org/bots/api#ownedgifts
 * 
 * @param totalCount The total number of gifts owned by the user or the chat
 * @param gifts The list of gifts
 * @param nextOffset Optional. Offset for the next request. If empty, then there are no more results.
 */
@Serializable
data class OwnedGifts(
    @SerialName("total_count") val totalCount: Long,
    val gifts: List<OwnedGift>,
    @SerialName("next_offset") val nextOffset: String? = null
)

